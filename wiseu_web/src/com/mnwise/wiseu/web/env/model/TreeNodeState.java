package com.mnwise.wiseu.web.env.model;

public class TreeNodeState {
    private boolean opened;
    private boolean disabled;
    private boolean selected;

    public TreeNodeState() {
    }

    public TreeNodeState(boolean opened, boolean disabled, boolean selected) {
        this.opened = opened;
        this.disabled = disabled;
        this.selected = selected;
    }

    public boolean getOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
