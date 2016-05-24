package es.agustruiz.tddm.model;

public class Notification {

    protected Long mId;
    protected String mTitle;
    protected String mMessage;
    protected Long mTimestamp;

    //region [Public methods]

    public Notification(){
        mId = null;
        mTitle = null;
        mMessage = null;
        mTimestamp = System.currentTimeMillis();
    }

    public Notification(Long id, String title, String message, Long timestamp) {
        mId = id;
        mTitle = title;
        mMessage = message;
        mTimestamp = timestamp;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public Long getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(Long mTimestamp) {
        this.mTimestamp = mTimestamp;
    }

    //endregion

}
