package com.bit.backend.dtos;

public class ServiceCategoryDto {
    private long id;
    private String categoryName;
    private Number displayOrder;
    private String description;

    public ServiceCategoryDto(long id, String categoryName, Number displayOrder, String description) {
        this.id = id;
        this.categoryName = categoryName;
        this.displayOrder = displayOrder;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Number getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Number displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceName() {
        return categoryName;
    }
}
