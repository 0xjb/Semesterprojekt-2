package business;

import acq.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Bruger
 */
public class BusinessFacade implements IBusiness {

    private static final int ID_LENGTH = 10;

    private static final int PASSWORD_MIN_LENGTH = 4;
    private static final int PASSWORD_MAX_LENGTH = 16;

    private static final int USERNAME_MIN_LENGTH = 4;
    private static final int USERNAME_MAX_LENGTH = 16;

    private static final int NAME_MAX_LENGTH = 100;
    private static final int MAIL_MAX_LENGTH = 50;

    private IData data;

    private SecurityHandler security;

    private ObservableList<IUser> users;

    private ObservableList<IInquiry> inquiries;

    private ObservableList<ICitizen> citizens;

    private ObservableList<ICase> cases;

    @Override
    public ObservableList<ICase> getCases() {
        ArrayList<ICase> cases = new ArrayList<>();
        for (ICitizen ic : citizens) {
            if (ic.getCase() != null) {
                cases.add(ic.getCase());
            }
        }
        return this.cases = FXCollections.observableArrayList(cases);
    }

    @Override
    public ObservableList<IUser> getUsers() {
        ArrayList<IUser> users = new ArrayList<>();
        ////FileManager
//          data.loadData(users, "users");
//          return this.users = FXCollections.observableArrayList(users);
        ////SQL
        List<String[]> list = data.readUsers();
        for (String[] array : list) {
            User user = null;
            if (Integer.parseInt(array[5]) == SystemAdmin.getAdminRole()) {
                user = new SystemAdmin(array[0], array[1], array[2], array[3], array[4]);
            } else if (Integer.parseInt(array[5]) == SocialWorker.getSWRole()) {
                user = new SocialWorker(array[0], array[1], array[2], array[3], array[4]);
            }
            if (user != null) {
                users.add(user);
            }
        }
        return this.users = FXCollections.observableArrayList(users);
    }

    @Override
    public ObservableList<ICitizen> getCitizen() {
        ArrayList<ICitizen> citizens = new ArrayList<>();
        data.loadData(citizens, "citizens");
        return this.citizens = FXCollections.observableArrayList(citizens);
    }

    @Override
    public ObservableList<IInquiry> getInquiries() {
        ArrayList<IInquiry> inquiries = new ArrayList<>(); // to be filled with inquiries
        for (ICitizen c : citizens) { // get all inquiries belonging to citizens
            inquiries.add(c.getInquiry()); // put them in the inquiries list
        }
        // set observablelist to be = inquiries and then return it
        return this.inquiries = FXCollections.observableArrayList(inquiries);
    }

    public BusinessFacade() {
    }

    @Override
    public void logOutActiveUser() {
        security.logData("logged out.");
        security.logOutActiveUser();

    }

    /**
     * A method to inject the data layer into the business layer
     *
     * @param dataLayer
     */
    @Override
    public void injectData(IData dataLayer) {
        data = dataLayer;
        security = new SecurityHandler(data);
        //tester(); //creates citizens with inquires
    }

    /**
     * a method to create a user in the system
     *
     * @param name name of the user
     * @param id id of the user
     * @param userName the username of the user
     * @param password the password for the user
     * @param email the email for the user
     * @param type the type of user: 0 for SystemAdmin, 1 for SocialWorker
     */
    @Override
    public void createUser(String name, String id, String userName, String password, String email, int type) {
        IUser user;
        if (security.getActiveUser() instanceof SystemAdmin) {
            user = ((SystemAdmin) security.getActiveUser()).createUser(name, id, userName, password, email, type);
            data.saveUsers(user);
//            if (user != null) {
//                ArrayList<IUser> users = data.readUsers();
//                users.add(user);
//                data.saveUsers(users);
//                users.add(user);
//                data.saveData((ArrayList<IUser>) users.stream().collect(Collectors.toList()), "users");
            security.logData("Created user: " + user.toString());
//            }
        } else {
            System.out.println("error, could not create user");
        }
    }

    /**
     * a method to delete a user from the system
     *
     * @param user
     * @param users
     */
    @Override
    public void deleteUser(IUser user) {
        if (security.getActiveUser() instanceof SystemAdmin) {
            if (((SystemAdmin) security.getActiveUser()).deleteUser(user, users)) {
                data.deleteUser(user);
                security.logData("Deleted user " + user.toString());
//                data.saveUsers(user);
                data.saveData((ArrayList<IUser>) users.stream().collect(Collectors.toList()), "users");
            } else {
                System.out.println("User did not exist");
            }
        }
    }

    /**
     * a method to validate the username and password of a user
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public boolean validateUser(String username, String password) {
        ArrayList<IUser> users = new ArrayList<>();
        data.loadData(users, "users");
        if (security.validateUserLogin(users, username, password)) {
            security.logData(username + " logged in.");
            return true;

        } else {

            return false;
        }
    }

    @Override
    public void saveInquiry(IInquiry inquiry) {
        ArrayList<ICitizen> citizens = new ArrayList<>();
        data.loadData(citizens, "citizens");
        Citizen c = inquiry.getCitizen();
        citizens.remove(c);
        c.setInquiry((Inquiry) inquiry);
        citizens.add(c);
        security.logData("Saved inquiry: " + c.toString());
        data.saveData(citizens, "citizens");
    }

//    public void tester() {
//        ArrayList<ICitizen> citizens = new ArrayList<>();
//        for (int i = 1; i <= 10; i++) {
//            citizens.add(new Citizen("Citizen" + (i), String.valueOf(i), "needs" + i));
//        }
//        for (ICitizen c : citizens) {
//            c.createInquiry(String.valueOf(c.getId()), "origin" + c.getId(), true, "description");
//        }
//        data.saveCitizens(citizens);
//    }
    @Override
    public int getRole() {
        return security.getActiveUser().getRole();
    }

    @Override
    public void createCase(String id, String des, String process, ISocialWorker sw, ICitizen c) {
        String s = "error, could not create case";
        ICase newCase;
        if (security.getActiveUser() instanceof SocialWorker) {
            newCase = ((ISocialWorker) security.getActiveUser()).createCase(id, des, process, sw, c);
            c.setCase((Case) newCase);
            if (newCase != null) {
                if (c.getCase() == null) {
                    cases.add(newCase);
                    c.setCase((Case) newCase);
                } else {

//                    cases.remove(c.getCase());
//                    c.setCase((Case)newCase);
//                    cases.add(newCase);
                }

                data.saveData((ArrayList<ICitizen>) citizens.stream().collect(Collectors.toList()), "citizens");
                security.logData("Created case with id: " + id);
            } else {
                System.out.println(s);
            }

        }
        System.out.println(c.getName() + id);
    }

    @Override
    public User getActiveUser() {
        return security.getActiveUser();
    }

    @Override
    public void deleteCase(ICase newCase) {
        if (security.getActiveUser() instanceof SocialWorker) {
            if (((SocialWorker) security.getActiveUser()).deleteCase(newCase)) {
                security.logData("Deleted case " + newCase.toString());
                data.saveData((ArrayList<ICitizen>) citizens.stream().collect(Collectors.toList()), "citizens");
            } else {
                System.out.println("Case did not exist");
            }
        }

    }

    @Override
    public void createCitizen(String name, String id, String needs) {
        ICitizen citizen;
        String s = "Error with Citizen";
        if (security.getActiveUser() instanceof SocialWorker) {
            citizen = ((ISocialWorker) security.getActiveUser()).createCitizen(name, id, needs);
            if (citizen != null) {
                citizens.add(citizen);
                data.saveData((ArrayList<ICitizen>) citizens.stream().collect(Collectors.toList()), "citizens");
                security.logData("Created Citizen: " + citizen.toString());
            } else {
                System.out.println(s);
            }

        }

    }

    @Override
    public void deleteCitizen(ICitizen citizen) {
        String s = "Error with Citizen";
        if (security.getActiveUser() instanceof SocialWorker) {
            if (((SocialWorker) security.getActiveUser()).deleteCitizen(citizen, citizens)) {
                security.logData("Deleted citizens: " + citizen.toString());
                data.saveData((ArrayList<ICitizen>) citizens.stream().collect(Collectors.toList()), "citizens");
            } else {
                System.out.println(s);
            }
        }
    }

    @Override
    public void createInquiry(String id, String origin, boolean informed, ICitizen citizen, String description) {
        IInquiry inquiry;
        String s = "Error with Citizen";
        if (security.getActiveUser() instanceof SocialWorker) {
            inquiry = ((ISocialWorker) security.getActiveUser()).createInquiry(id, origin, informed, citizen, description);
            if (citizen != null) {
                citizen.setInquiry((Inquiry) inquiry);
                data.saveData((ArrayList<ICitizen>) citizens.stream().collect(Collectors.toList()), "Citizens");
                security.logData("Created Inquiry: " + citizen.getInquiry().toString());
            } else {
                System.out.println(s);
            }

        }
    }

    @Override
    public void deleteInquiry(IInquiry i) {
        String s = "Error with Citizen";
        if (security.getActiveUser() instanceof SocialWorker) {
            if (((SocialWorker) security.getActiveUser()).deleteInquiry(i)) {
                security.logData("Deleted inquiry: " + i.toString());
                data.saveData((ArrayList<ICitizen>) citizens.stream().collect(Collectors.toList()), "citizens");
            } else {
                System.out.println(s);
            }
        }
    }

    @Override
    public void editCase(String description, String process, ICase c) {
        c.setDescription(description);
        c.setProcess(process);
        security.logData("Edited case: " + c.toString());
        data.saveData((ArrayList<ICitizen>) citizens.stream().collect(Collectors.toList()), "citizens");
    }

    @Override
    public void editCitizen(String needs, ICitizen c) {
        c.setNeeds(needs);
        security.logData("Edited citizen: " + c.toString());
        data.saveData((ArrayList<ICitizen>) citizens.stream().collect(Collectors.toList()), "citizens");
    }

    @Override
    public void editInquiry(String description, IInquiry i, boolean isInformed) {
        i.setDescription(description);
        i.setIsCitizenInformed(isInformed);
        security.logData("Edited inquiry: " + i.toString());
        data.saveData((ArrayList<ICitizen>) citizens.stream().collect(Collectors.toList()), "citizens");
    }

    // changed when database is added!!!
    @Override
    public boolean hasUniqueUserID(String id) {
        // return data.hasUniqueUserUD(id);
        return true;
    }

    // changed when database is added!!!
    @Override
    public boolean hasUnqiueCitizenID(String id) {
        // return data.hasUniqueCitizenID(id);
        return true;
    }

    @Override
    public boolean hasAcceptableID(String id) {
        try {
            if (id.length() == ID_LENGTH) {
                return true;
            }
        } catch (NullPointerException ex) {
        }
        return false;
    }

    @Override
    public boolean hasAcceptablePassword(String password, String repeatedPassword) {
        try {
            if (password.equals(repeatedPassword)) {
                if (password.length() >= PASSWORD_MIN_LENGTH && password.length() <= PASSWORD_MAX_LENGTH) {
                    return true;
                }
            }
        } catch (NullPointerException ex) {
        }
        return false;
    }

    @Override
    public boolean hasAcceptableUsername(String username) {
        try {
            if (username.length() >= USERNAME_MIN_LENGTH && username.length() <= USERNAME_MAX_LENGTH) {
                return true;
            }
        } catch (NullPointerException ex) {
        }
        return false;
    }

    @Override
    public boolean hasAcceptableMail(String mail) {
        try {
            if (mail.length() <= MAIL_MAX_LENGTH && mail.contains("@")) {
                return true;
            }
        } catch (NullPointerException ex) {
        }
        return false;
    }

    @Override
    public boolean hasAcceptableName(String name) {
        try {
            if (name.length() >= 1 && name.length() <= NAME_MAX_LENGTH) {
                return true;
            }
        } catch (NullPointerException ex) {
        }
        return false;
    }

}
