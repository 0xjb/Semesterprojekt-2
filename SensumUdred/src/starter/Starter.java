package starter;

import acq.IBusiness;
import acq.IData;
import acq.IPresentation;
import business.BusinessFacade;
import data.DataFacade;
import presentation.PresentationFacade;

/**
 *
 * @author Søren Bendtsen
 */
public class Starter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code applicastiteston logic here
        IData data = new DataFacade();
        IBusiness business = new BusinessFacade();
        IPresentation ui = new PresentationFacade();
        // Data is injected into business layer.
        business.injectData(data);
        // Business is injecting into UI layer.
        ui.injectBusiness(business);
        
        // Start GUI.
        ui.openUI();
    }

    }


