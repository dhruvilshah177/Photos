package Photos.model;

import java.io.Serializable;
import java.time.LocalDate;


/**
 * This class represents the conditions for date time search
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Time_Search_Condition implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6373163668095808715L;


    /**
     * Start date for rangr
     */
    private LocalDate startDate = null;

    /**
     * End date for range
     */
    private LocalDate endDate = null;

	
	public Time_Search_Condition() {

	}

    /**
     * @return End date
     */
    public LocalDate getStartDate() {
		return startDate;
	}


    /**
     * @param startDate Start date
     */
    public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}


    /**
     * @return End date
     */
    public LocalDate getEndDate() {
		return endDate;
	}


    /**
     * @param endDate End date
     */
    public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
}