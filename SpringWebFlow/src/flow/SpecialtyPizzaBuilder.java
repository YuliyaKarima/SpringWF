package flow;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import pizza.Pizza;
import pizza.PizzaToppings;
import pizza.Topping;

public class SpecialtyPizzaBuilder implements Action {
    private static final Logger LOGGER = Logger
            .getLogger(SpecialtyPizzaBuilder.class);

    public Event execute(RequestContext request) throws Exception {
        String type = request.getRequestParameters().get("pizzaType");

        LOGGER.debug("BUILDING A SPECIALTY PIZZA:  " + type);

        Pizza pizza = (Pizza) request.getFlowScope().get("pizza");
        if ("MEAT".equals(type)) {
            LOGGER.debug("BUILDING A CARNIVORE");

            Set<PizzaToppings> meats = new HashSet<PizzaToppings>();

            meats.add(new PizzaToppings(Topping.CANADIAN_BACON.toString()));
            meats.add(new PizzaToppings(Topping.HAMBURGER.toString()));
            meats.add(new PizzaToppings(Topping.PEPPERONI.toString()));
            meats.add(new PizzaToppings(Topping.SAUSAGE.toString()));

            pizza.setToppings(meats);
        } else if ("VEGGIE".equals(type)) {
            LOGGER.debug("BUILDING A HERBIVORE");
            Set<PizzaToppings> meats = new HashSet<PizzaToppings>();

            meats.add(new PizzaToppings(Topping.GREEN_PEPPER.toString()));
            meats.add(new PizzaToppings(Topping.MUSHROOM.toString()));
            meats.add(new PizzaToppings(Topping.PINEAPPLE.toString()));
            meats.add(new PizzaToppings(Topping.TOMATO.toString()));

            pizza.setToppings(meats);
        } else if ("THEWORKS".equals(type)) {
            LOGGER.debug("BUILDING AN OMNIVORE");

            Set<PizzaToppings> meats = new HashSet<PizzaToppings>();
            System.out.println("THE WORKS!");

            meats.add(new PizzaToppings(Topping.CANADIAN_BACON.toString()));
            meats.add(new PizzaToppings(Topping.HAMBURGER.toString()));
            meats.add(new PizzaToppings(Topping.PEPPERONI.toString()));
            meats.add(new PizzaToppings(Topping.SAUSAGE.toString()));
            meats.add(new PizzaToppings(Topping.GREEN_PEPPER.toString()));
            meats.add(new PizzaToppings(Topping.MUSHROOM.toString()));
            meats.add(new PizzaToppings(Topping.PINEAPPLE.toString()));
            meats.add(new PizzaToppings(Topping.TOMATO.toString()));
            meats.add(new PizzaToppings(Topping.EXTRA_CHEESE.toString()));
            meats.add(new PizzaToppings(Topping.ONION.toString()));
            meats.add(new PizzaToppings(Topping.JALAPENO.toString()));

            pizza.setToppings(meats);
        }

        request.getFlowScope().put("pizza", pizza);

        return new Event(this, "success");
    }
}
