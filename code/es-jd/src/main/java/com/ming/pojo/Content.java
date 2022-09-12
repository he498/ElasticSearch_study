package com.ming.pojo;

/**
 * @version v1.0
 * @ClassNmae: Content
 * @Description:
 * @Author: Ming on 2022/9/6
 */

public class Content {
    private String title;
    private String img;
    private String price;

    public Content(String title, String img, String price) {
        this.title = title;
        this.img = img;
        this.price = price;
    }

    public Content() {
    }

    @Override
    public String toString() {
        return "Content{" +
                "title='" + title + '\'' +
                ", img='" + img + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
