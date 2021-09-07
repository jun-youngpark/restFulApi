package com.mnwise.wiseu.web.env.model;

public class EnvDomainVo {
    private String domain;
    private String oldDomain;
    private String newDomain;

    private String domains;

    public String getDomains() {
        return domains;
    }

    public void setDomains(String domains) {
        this.domains = domains;
    }

    public String getOldDomain() {
        return oldDomain;
    }

    public void setOldDomain(String aldDomain) {
        this.oldDomain = aldDomain;
    }

    public String getNewDomain() {
        return newDomain;
    }

    public void setNewDomain(String newDomain) {
        this.newDomain = newDomain;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

}
