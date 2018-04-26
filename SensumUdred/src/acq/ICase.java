/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acq;

/**
 *
 * @author Frederik
 */
public interface ICase {
    public String getDescription();

    public void setDescription(String description);

    public String getProcess();

    public void setProcess(String process);

    public ISocialWorker getSocialWorker();

    public void setSocialWorker(ISocialWorker socialWorker);

    public ICitizen getCitizen();

    public void setCitizen(ICitizen citizen);

}
