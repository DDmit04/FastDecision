package hackaton.fastdisision.views;

/**
 * Jackson view class
 * @author Dmitrochenkov Daniil
 * @version 1.0
 */
public class VotingView {

    /**
     * ID of entity
     */
    public interface Id {}

    /**
     * Data to display in charts
     */
    public interface ChartData extends Id {}

    /**
     * normal data for sent to frontend
     */
    public interface MinimalData extends ChartData {}

    /**
     * data for send to reliable requests (admin requests, voting creation request)
     */
    public interface CoreData extends MinimalData {}

    /**
     * all fields of entity thad should never send to frontend
     */
    public interface FullData extends CoreData {}

}
