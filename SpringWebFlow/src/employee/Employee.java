package employee;

import java.util.Date;
import java.util.Set;

import user.User;

public interface Employee {
String getName();
Date getDob();
String getAddress();
Set<User> getUser();
}
