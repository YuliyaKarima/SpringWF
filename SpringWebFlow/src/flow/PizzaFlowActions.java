package flow;

import static org.apache.log4j.Logger.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.elasticsearch.ElasticsearchQueries;
import org.hibernate.search.query.engine.spi.QueryDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import cook.Cooker;
import dao.BookingDao;
import dao.CookDao;
import dao.CourierDao;
import dao.CustomerDao;
import dao.DeliveryDao;
import dao.IngredientDao;
import dao.OrderDao;
import dao.PizzaDao;
import dao.PizzaTypeDao;
import dao.TableDao;
import facebook.FBConnection;
import ingredient.Ingredient;
import pizza.*;
import twitter.MyAccessToken;
import twitter.OAuthToken;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.RequestToken;

@Component
public class PizzaFlowActions {
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	CustomerService customerService;

	OrderService orderService;

	private static User user;

	private static final Logger LOGGER = getLogger(PizzaFlowActions.class);

	private static ApplicationContext context = new ClassPathXmlApplicationContext("hibernate.xml");

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private static List<Bookings> newBookings;

	private static List<Delivery> newDeliveries;

	private List<Pizza> pizzaToCook = new ArrayList<>();

	private List<Pizza> pizzaToCookBookings = new ArrayList<>();

	private List<Pizza> pizzaToCookDelivery = new ArrayList<>();

	private List<Pizza> pizzaCooking = new ArrayList<>();

	private List<Pizza> pizzaCookingBookings = new ArrayList<>();

	private List<Pizza> pizzaCookingDelivery = new ArrayList<>();

	private List<Pizza> pizzaFromBookings = new ArrayList<>();

	private List<Pizza> pizzaFromDeliveries = new ArrayList<>();

	/**
	 * Find customer by telephone number entered in the form.
	 * 
	 * @param phoneNumber
	 * @return Customer
	 * @throws CustomerNotFoundException
	 */
	public Customer lookupCustomer(String phoneNumber) throws CustomerNotFoundException {
		Customer customer = customerService.lookupCustomer(phoneNumber);
		if (customer != null) {
			return customer;
		} else {
			throw new CustomerNotFoundException();
		}
	}

	public static ApplicationContext getContext() {
		return context;
	}

	/**
	 * Save customer to DB.
	 * 
	 * @param customer
	 */
	public void addCustomer(Customer customer) {
		LOGGER.warn("Add new customer " + customer.getName());
		CustomerDao dao = (CustomerDao) context.getBean("customerDao");
		dao.save(customer);
	}

	/**
	 * Analyses PaymentDetails that came from user form
	 * 
	 * @param paymentDetails
	 * @return Payment implementation object.
	 */
	public Payment verifyPayment(PaymentDetails paymentDetails) {
		Payment payment = null;
		if (paymentDetails.getPaymentType() == PaymentType.CREDIT_CARD) {
			payment = new CreditCardPayment();
		} else {
			payment = new CashOrCheckPayment();
		}
		return payment;
	}

	/**
	 * Save pizza's details and order.
	 * 
	 * @param order
	 */
	public void saveOrder(Order order) {
		LOGGER.warn("Saving  pizzas: " + order.getPizzas().size() + " pizza");
		if (order instanceof Bookings) {
			LOGGER.warn("Table: " + ((Bookings) order).getTable());
		}
		PizzaDao pizzaDao = (PizzaDao) context.getBean("pizzaDao");

		LOGGER.warn("Order details: " + order.getPizzas().size() + " pizza");
		LOGGER.warn("Customer: " + order.getCustomer().getName());
		LOGGER.warn("Payment type: " + order.getPayment());
		// save every pizza from order
		for (Pizza pizza : order.getPizzas()) {
			pizzaDao.save(pizza);
		}
		order.setDateOfOrder(new Date());
		order.getPayment().setAmount(countSum(order));
		order.setCosts(countCosts(order));
		OrderDao orderDao = (OrderDao) context.getBean("orderDao");
		orderDao.save(order);

		LOGGER.warn("Order saved - " + order.getCustomer().getName());
	}

	/**
	 * Check if customer's address zip code is in delivery area
	 * 
	 * @param zipCode
	 * @return true if customer's address zip code is in delivery area
	 */
	public boolean checkDeliveryArea(String zipCode) {
		LOGGER.warn("Check for sip code. " + zipCode);
		return "75075".equals(zipCode);
	}

	/**
	 * count order price
	 * 
	 * @param order
	 * @return
	 */
	public float countSum(Order order) {
		PricingEngine pricingEngine = (PricingEngine) context.getBean("pricingEngine");
		return pricingEngine.calculateOrderTotal(order);
	}

	public float countCosts(Order order) {
		CostsEngine costsEngine = (CostsEngine) context.getBean("costsEngine");
		return costsEngine.calculateCostsTotal(order);
	}

	public List<Place> getFreeTables() {
		TableDao dao = (TableDao) context.getBean("tableDao");
		return dao.freeTables();
	}

	public List<Place> getFreeTables(LocalDateTime forDate) {
		ApplicationContext context = new ClassPathXmlApplicationContext("hibernate.xml");
		TableDao dao = (TableDao) context.getBean("tableDao");
		return dao.freeTables(forDate);
	}

	public Place getTable(String name) {
		int tableID = Integer.parseInt(name);
		TableDao dao = (TableDao) context.getBean("tableDao");
		Place place = dao.tableByID(tableID);
		LOGGER.warn("Table: " + name + " " + tableID);
		return place;
	}

	public void getPlace(Place place) {
		LOGGER.warn("Table: " + place);
	}

	public static User getUser() {
		return user;
	}

	public static void setUser(User user) {
		PizzaFlowActions.user = user;
	}

	public String twitterSettings() {
		OAuthToken oauthToken = (OAuthToken) context.getBean("oauthToken");
		MyAccessToken accestoken = (MyAccessToken) context.getBean("accessToken");
		String authUrl = null;
		LOGGER.warn("Twitter settings");
		twitter4j.conf.ConfigurationBuilder cb = new twitter4j.conf.ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(oauthToken.getConsumerKey())
				.setOAuthConsumerSecret(oauthToken.getConsumerSecret()).setOAuthAccessToken(null)
				.setOAuthAccessTokenSecret(null).setOAuthRequestTokenURL("https://api.twitter.com/oauth/request_token")
				.setOAuthAuthorizationURL("https://api.twitter.com/oauth/authorize")
				.setOAuthAccessTokenURL("https://api.twitter.com/oauth/access_token");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();

		RequestToken requestToken;
		try {
			String callbackURL = "http://localhost:8090/SpringWebFlow/login/twitter";
			requestToken = twitter.getOAuthRequestToken(callbackURL);
			String token = requestToken.getToken();
			String tokenSecret = requestToken.getTokenSecret();
			accestoken.setTokensecret(tokenSecret);
			accestoken.setToken(token);
			authUrl = requestToken.getAuthorizationURL();
		} catch (TwitterException e) {
			LOGGER.warn("Error while obtaining token ");
			e.printStackTrace();
		}
		return authUrl;
	}

	public static String twitterUser() {
		LOGGER.warn("twitter user " + (user != null ? user.getName() : "not registered yet"));
		return user != null ? user.getName() : null;
	}

	public String facebookSettings() {
		String authUrl = null;
		FBConnection fbConnection = new FBConnection();
		authUrl = fbConnection.getFBAuthUrl();
		return authUrl;
	}

	public String find(String find) {
		Session session = sessionFactory.openSession();
		FullTextSession ft = Search.getFullTextSession(session);
		Transaction tx = ft.beginTransaction();

		QueryDescriptor query = ElasticsearchQueries.fromJson(
				// "{\"query\": {\"match_all\": {}}}")
				"{ \"query\": { \"match_phrase_prefix\": { \"pizzaType.name\":" + find.toUpperCase() + " } } }");

		LOGGER.warn("Elasticsearch query ");

		List<Pizza> pizzas = ft.createFullTextQuery(query, Pizza.class).getResultList();

		StringBuilder pizzasIds = new StringBuilder("(");
		for (int i = 0; i < pizzas.size(); i++) {
			pizzasIds.append(pizzas.get(i).getId());
			if (i < pizzas.size() - 1) {
				pizzasIds.append(", ");
			}
		}
		pizzasIds.append(");");
		LOGGER.warn(pizzasIds);

		LOGGER.warn("Pizza from db: " + ft.load(Pizza.class, pizzas.get(0).getId()).getPizzaType().getName());

		String sqlQuery = "select * from pizza_store.pizza_associassion join pizza_store.bookings on pizza_store.pizza_associassion.order_id = pizza_store.bookings.id "
				+ "join pizza_store.cash_payment on pizza_store.bookings.payment_id = pizza_store.cash_payment.id "
				+ "where pizza_store.cash_payment.summ < 9 and pizza_store.pizza_associassion.id in " + pizzasIds;

		LOGGER.warn(sqlQuery);

		LOGGER.warn(ft.createNativeQuery(sqlQuery).addEntity(Pizza.class).list().size());
		// List<Bookings> bookings = ft.createFullTextQuery(query,
		// Bookings.class).getResultList();
		tx.commit();
		// String customerName = bookings.get(0).getCustomer().getName();
		session.close();

		return pizzas.size() == 0 ? "not found" : pizzas.get(0).getPizzaType().getName();
	}

	public void updateBookings(BookingsWrapper orderWrapper) {
		LOGGER.warn("Saving bookings updates");
		BookingDao bookingDao = (BookingDao) context.getBean("bookingDao");

		orderWrapper.getBookingList().forEach(booking -> {
			if (booking.getIsConfirmed() == true) {
				bookingDao.confirm(booking.getId());
				pizzaFromBookings.addAll(booking.getPizzas());
			}
		});
		newBookings = orderWrapper.getBookingList();

	}

	public void updateDeliveries(DeliveriesWrapper orderWrapper, List<Express> expresses) {
		LOGGER.warn("Saving deliveries updates");

		DeliveryDao deliveryDao = (DeliveryDao) context.getBean("deliveryDao");

		orderWrapper.getDeliveryList().forEach(delivery -> {
			if (!delivery.getExpressName().isEmpty()) {
				deliveryDao.setExpress(delivery.getId(), Integer.parseInt(delivery.getExpressName()));
				pizzaFromDeliveries.addAll(delivery.getPizzas());
			}
		});
		newDeliveries = orderWrapper.getDeliveryList();
	}

	public List<Express> getExpressList() {
		CourierDao dao = (CourierDao) context.getBean("expressDao");
		return dao.findFree();
	}

	public BookingsWrapper getNewBookings() {
		BookingDao dao = (BookingDao) context.getBean("bookingDao");
		BookingsWrapper bw = new BookingsWrapper();
		List<Bookings> list = dao.list();
		bw.setBookingList(list);
		LOGGER.warn("New bookings: " + list.size());
		for (Bookings booking : list) {
			LOGGER.warn(booking.getId());
		}
		newBookings = list;
		return bw;
	}

	public DeliveriesWrapper getNewDeliveries() {
		DeliveryDao dao = (DeliveryDao) context.getBean("deliveryDao");
		DeliveriesWrapper dw = new DeliveriesWrapper();
		List<Delivery> list = dao.newDeliveries();
		dw.setDeliveryList(list);
		LOGGER.warn("New deliveries: " + list.size());
		for (Delivery delivery : list) {
			LOGGER.warn(delivery.getId());
		}
		newDeliveries = list;
		return dw;
	}

	public void confirmBooking(String id) {
		LOGGER.warn("confirmBooking");
		BookingDao dao = (BookingDao) context.getBean("bookingDao");
		dao.confirm(Integer.parseInt(id));
	}

	public List<Bookings> getBookings() {
		return newBookings;
	}

	public List<Bookings> getConfirmedBookings() {
		List<Bookings> confirmedBookings = new ArrayList<>();
		newBookings.forEach(booking -> {
			if (booking.getIsConfirmed() == true) {
				confirmedBookings.add(booking);
			}
		});
		return confirmedBookings;
	}

	public List<Pizza> getBookingsPizzas() {
		return pizzaFromBookings;
	}

	public List<Pizza> getDeliveriePizzas() {
		return pizzaFromDeliveries;
	}

	public List<Delivery> getDeliveries() {
		return newDeliveries;
	}

	public void sendSelectBookingsPizzasToCook(String selected) {
		String[] ids = selected.split(",");
		List<Integer> intId = new ArrayList<>();
		for (int i = 0; i < ids.length; i++) {
			intId.add(Integer.parseInt(ids[i]));
		}

		pizzaFromBookings.forEach(pizza -> {
			if (intId.contains(pizza.getId())) {
				pizza.setDateToBeCooked(LocalDateTime.now().plusMinutes(30));
				pizza.setStatus("WAIT_FOR_COOK");
				pizzaToCookBookings.add(pizza);
			}
		});
		sendSelectPizzasToCook(intId, pizzaFromBookings);

	}

	public void sendSelectDeliveriesPizzasToCook(String selected) {
		String[] ids = selected.split(",");
		List<Integer> intId = new ArrayList<>();
		for (int i = 0; i < ids.length; i++) {
			intId.add(Integer.parseInt(ids[i]));
		}
		pizzaFromDeliveries.forEach(pizza -> {
			if (intId.contains(pizza.getId())) {
				pizza.setDateToBeCooked(LocalDateTime.now().plusMinutes(30));
				pizza.setStatus("WAIT_FOR_COOK");
				pizzaToCookDelivery.add(pizza);
			}
		});
		sendSelectPizzasToCook(intId, pizzaFromDeliveries);
	}

	public void sendSelectPizzasToCook(List<Integer> intId, List<Pizza> pizzas) {

		PizzaDao pizzaDao = (PizzaDao) context.getBean("pizzaDao");

		List<Pizza> toRemove = new ArrayList<>();
		pizzas.forEach(pizza -> {
			if (intId.contains(pizza.getId())) {
				pizzaDao.update(pizza);
				pizzaToCook.add(pizza);
				toRemove.add(pizza);
			}
		});

		for (int i = 0; i < toRemove.size(); i++) {
			pizzas.remove(toRemove.get(i));
		}
	}

	public void sendBookingPizzasToCook() {
		pizzaToCookBookings.addAll(pizzaFromBookings);
		sendPizzasToCook(pizzaFromBookings);
	}

	public void sendDeliveryPizzasToCook() {
		pizzaToCookDelivery.addAll(pizzaFromDeliveries);
		sendPizzasToCook(pizzaFromDeliveries);
	}

	public List<Pizza> getNewPizzas() {
		return pizzaToCook;
	}

	private void sendPizzasToCook(List<Pizza> pizzas) {
		PizzaDao pizzaDao = (PizzaDao) context.getBean("pizzaDao");
		pizzas.forEach(pizza -> {
			pizza.setDateToBeCooked(LocalDateTime.now().plusMinutes(30));
			pizza.setStatus("WAIT_FOR_COOK");
			pizzaDao.update(pizza);
			pizzaToCook.add(pizza);
		});
		pizzas.clear();
	}

	public String dateNotBefore() {
		LOGGER.warn(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).replace(" ", "T"));
		LOGGER.warn(LocalDateTime.now().toString());
		return LocalDateTime.now().plusHours(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).replace(" ",
				"T");
	}

	public boolean checkIfDelivery(Order order) {
		return order instanceof Delivery;
	}

	public void cookAll() {
		PizzaDao pizzaDao = (PizzaDao) context.getBean("pizzaDao");
		pizzaToCook.forEach(pizza -> {
			pizza.setStatus("COOKING");
			pizzaDao.update(pizza);
			pizzaCooking.add(pizza);
		});
		pizzaCookingBookings.addAll(pizzaToCookBookings);
		pizzaToCookBookings.clear();
		pizzaCookingDelivery.addAll(pizzaToCookDelivery);
		pizzaToCookDelivery.clear();
		pizzaToCook.clear();
	}

	public void cookSelected(String line) {
		String[] ids = line.split(",");
		List<Integer> intId = new ArrayList<>();
		for (int i = 0; i < ids.length; i++) {
			intId.add(Integer.parseInt(ids[i]));
		}
		PizzaDao pizzaDao = (PizzaDao) context.getBean("pizzaDao");
		IngredientDao ingredientDao = (IngredientDao) context.getBean("ingredientDao");

		List<Pizza> toRemove = new ArrayList<>();
		List<Pizza> toRemoveBookings = new ArrayList<>();
		List<Pizza> toRemoveDelivery = new ArrayList<>();

		pizzaToCook.forEach(pizza -> {
			if (intId.contains(pizza.getId())) {
				pizza.setStatus("COOKING");
				pizzaDao.update(pizza);
				ingredientDao.get(pizza);
				pizzaCooking.add(pizza);
				toRemove.add(pizza);
			}
		});

		pizzaToCookBookings.forEach(pizza -> {
			if (intId.contains(pizza.getId())) {
				pizzaCookingBookings.add(pizza);
				toRemoveBookings.add(pizza);
			}
		});

		pizzaToCookDelivery.forEach(pizza -> {
			if (intId.contains(pizza.getId())) {
				pizzaCookingDelivery.add(pizza);
				toRemoveDelivery.add(pizza);
			}
		});

		for (int i = 0; i < toRemove.size(); i++) {
			pizzaToCook.remove(toRemove.get(i));
		}
		for (int i = 0; i < toRemoveBookings.size(); i++) {
			pizzaToCookBookings.remove(toRemoveBookings.get(i));
		}
		for (int i = 0; i < toRemoveDelivery.size(); i++) {
			pizzaToCookDelivery.remove(toRemoveDelivery.get(i));
		}
	}

	public List<Pizza> getCookingPizzas() {
		return pizzaCooking;
	}

	public List<Pizza> getCookingPizzasBookings() {
		return pizzaCookingBookings;
	}

	public List<Pizza> getCookingPizzasDelivery() {
		return pizzaCookingDelivery;
	}

	public List<Pizza> getPizzasToCook() {
		PizzaDao pizzaDao = (PizzaDao) context.getBean("pizzaDao");
		List<Pizza> toCook = pizzaDao.toCook();
		pizzaToCook.addAll(toCook);
		return toCook;
	}

	public List<Pizza> getPizzasToCookDelivery() {
		return pizzaToCookDelivery;
	}

	public List<Pizza> getPizzasToCookBookings() {
		return pizzaToCookBookings;
	}

	public void cooked(String line) {
		String[] ids = line.split(",");
		List<Integer> intId = new ArrayList<>();
		for (int i = 0; i < ids.length; i++) {
			intId.add(Integer.parseInt(ids[i]));
		}
		PizzaDao pizzaDao = (PizzaDao) context.getBean("pizzaDao");
		List<Pizza> toRemove = new ArrayList<>();
		pizzaCooking.forEach(pizza -> {
			if (intId.contains(pizza.getId())) {
				pizza.setStatus("COOKED");
				pizzaDao.update(pizza);
				toRemove.add(pizza);
			}
		});

		for (int i = 0; i < toRemove.size(); i++) {
			pizzaCooking.remove(toRemove.get(i));
		}
	}

	public void deliver(String line) {
		String[] ids = line.split(",");
		List<Integer> intId = new ArrayList<>();
		for (int i = 0; i < ids.length; i++) {
			intId.add(Integer.parseInt(ids[i]));
		}
	}

	public void newPizzaType(String typeName, String[] ingredientsIdList, String[] ingredientValueList,
		List<Ingredient> ingredients, CommonsMultipartFile file) {
		LOGGER.warn(file.getFileItem().getName());
		File image = new File(file.getFileItem().getName());
		String base = "D:/Julija/SpringWebFlow/WebContent/";
		image.renameTo(new File(base + typeName.toLowerCase() + ".jpg"));

		PizzaTypeDao pizzaTypeDao = (PizzaTypeDao) context.getBean("pizzaTypeDao");
		PizzaType type = new PizzaType();
		type.setName(typeName);
		for (int i = 0; i < ingredientsIdList.length; i++) {
			type.getIngredientsValue().put(findById(Integer.parseInt(ingredientsIdList[i]), ingredients),
					Double.parseDouble(ingredientValueList[i]));
		}
		pizzaTypeDao.save(type);
	}

	private Ingredient findById(int id, List<Ingredient> ingredients) {
		Ingredient returnIngredient = null;
		for (int i = 0; i < ingredients.size(); i++) {
			if (ingredients.get(i).getId() == id) {
				returnIngredient = ingredients.get(i);
			}
		}
		return returnIngredient;
	}

	public List<Ingredient> ingredients() {
		IngredientDao ingredientDao = (IngredientDao) context.getBean("ingredientDao");
		return ingredientDao.list();
	}

	public List<String> pizzaTypes() {
		PizzaTypeDao pizzaTypeDao = (PizzaTypeDao) context.getBean("pizzaTypeDao");
		List<PizzaType> types = pizzaTypeDao.list();
		return types.stream().map(type -> type.getName()).collect(Collectors.toList());
	}

	public List<PizzaType> pizzaTypesList() {
		PizzaTypeDao pizzaTypeDao = (PizzaTypeDao) context.getBean("pizzaTypeDao");
		List<PizzaType> types = pizzaTypeDao.list();
		return types;
	}

	public String toJsonTypes() {
		ObjectMapper objectMapper = new ObjectMapper();
		String listToJson = null;
		List<PizzaType> list = pizzaTypesList();
		list.forEach(type -> type.setName(type.getName().toLowerCase()));
		list.forEach(type -> type.setNutritionInfo());
		try {
			listToJson = objectMapper.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.warn(listToJson);
		return listToJson;
	}

	public String toJson(List<String> types) {
		ObjectMapper objectMapper = new ObjectMapper();
		String listToJson = null;
		try {
			listToJson = objectMapper
					.writeValueAsString(types.stream().map(type -> type.toLowerCase()).collect(Collectors.toList()));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.warn(listToJson);
		return listToJson;
	}	
	
	public void savePizza(Order order, String type, Pizza pizza) {
		pizza.setPizzaType(pizzaTypesList().stream().filter(t->t.getId()==Integer.parseInt(type)).findFirst().get());
		order.addPizza(pizza);
	}
	
	public void changeIngredientsCost(List<Ingredient> ingredients, String[] new_costs) {		
		IngredientDao ingredientDao = (IngredientDao) context.getBean("ingredientDao");
		for(int i = 0; i<ingredients.size(); i++) {
			if(ingredients.get(i).getCost()!=Double.parseDouble(new_costs[i])) {
				ingredients.get(i).setCost(Double.parseDouble(new_costs[i]));
				ingredientDao.update(ingredients.get(i));
			}
		}
	}
	
	public void saveCourier(Express courier) {
		CourierDao dao = (CourierDao) context.getBean("expressDao");
		dao.save(courier);
	}
	
    public void saveCooker(Cooker cooker) {
    	CookDao dao = (CookDao) context.getBean("cookerDao");
		dao.save(cooker);;
	}
}
