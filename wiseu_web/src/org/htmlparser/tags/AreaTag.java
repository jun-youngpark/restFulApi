package org.htmlparser.tags;

public class AreaTag extends CompositeTag {
    /**
     * 
     */
    private static final long serialVersionUID = 7385344607132380653L;

    private static final String[] mIds = new String[] {
        "area"
    };

    private static final String[] mEndTagEnders = new String[] {
        "map"
    };

    public AreaTag() {
    }

    public String[] getIds() {
        return(mIds);
    }

    public String[] getEnders() {
        return(mIds);
    }

    public String[] getEndTagEnders() {
        return(mEndTagEnders);
    }

    public String getHref() {
        return super.getAttribute("href");
    }

    public String getCoords() {
        return super.getAttribute("coords");
    }

    public String getShape() {
        return super.getAttribute("shape");
    }

    public String toString() {
        return mIds[0].toString();
    }

}