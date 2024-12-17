package logic;

import java.util.Date;

public class Borrow {

	private Date dateOfBorrow;
	private Date dueDate;
	private Date dateOfReturn;
	
	public Borrow(Date dateOfBorrow,Date dueDate,Date dateOfReturn) {
		this.dateOfBorrow=dateOfBorrow;
		this.dueDate=dueDate;
		this.dateOfReturn=dateOfReturn;
	}
	
    public Date getDateOfBorrow() {
        return dateOfBorrow;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Date getDateOfReturn() {
        return dateOfReturn;
    }

    public void setDateOfBorrow(Date dateOfBorrow) {
        this.dateOfBorrow = dateOfBorrow;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setDateOfReturn(Date dateOfReturn) {
        this.dateOfReturn = dateOfReturn;
    }
}
