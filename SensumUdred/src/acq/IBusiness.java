/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acq;

import java.util.ArrayList;
import javafx.collections.ObservableList;

/**
 *
 * @author Bruger
 */
public interface IBusiness {

    void injectData(IData dataLayer);

    public void createUser(String name, String id, String userName, String password, String email, int type);

    public void deleteUser(IUser user);
    
    public boolean validateUser(String username, String password);
    
    public ObservableList<IUser> getUsers();
    
    public ObservableList<IInquiry> getInquiries();
}
