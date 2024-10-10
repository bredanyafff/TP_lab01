package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {
    public static void main(String[] args) {
        try {
            File file = new File("random_structure_22.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            NodeList bookNodeList = document.getElementsByTagName("book");

            List<Book> bookList = new ArrayList<>();

            for (int i = 0; i < bookNodeList.getLength(); i++) {
                if (bookNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element bookElement = (Element) bookNodeList.item(i);

                    String id = bookElement.getAttribute("id");

                    String title = bookElement.getElementsByTagName("title").item(0).getTextContent();
                    String author = bookElement.getElementsByTagName("author").item(0).getTextContent();
                    String year = bookElement.getElementsByTagName("year").item(0).getTextContent();
                    String genre = bookElement.getElementsByTagName("genre").item(0).getTextContent();

                    // Работа с ценой
                    Element priceElement = (Element) bookElement.getElementsByTagName("price").item(0);
                    String priceValue = priceElement.getTextContent();
                    String currency = priceElement.getAttribute("currency");

                    // Создаем объект книги и устанавливаем поля
                    Book book = new Book();
                    book.setId(Integer.parseInt(id));
                    book.setTitle(title);
                    book.setAuthor(author);
                    book.setYear(Integer.parseInt(year));
                    book.setGenre(genre);
                    book.getPrice().setValue(Double.parseDouble(priceValue));
                    book.getPrice().setCurrency(currency);

                    // Парсинг издательства
                    NodeList publisherNode = bookElement.getElementsByTagName("publisher");
                    if (publisherNode.getLength() > 0) {
                        Element publisherElement = (Element) publisherNode.item(0);
                        String publisherName = publisherElement.getElementsByTagName("name").item(0).getTextContent();
                        String city = publisherElement.getElementsByTagName("city").item(0).getTextContent();
                        String country = publisherElement.getElementsByTagName("country").item(0).getTextContent();
                        book.setPublisher(new Publisher(publisherName, city, country));
                    }

                    // Парсинг обзоров
                    NodeList reviewsNodeList = bookElement.getElementsByTagName("review");
                    List<Review> reviews = new ArrayList<>();
                    for (int j = 0; j < reviewsNodeList.getLength(); j++) {
                        Element reviewElement = (Element) reviewsNodeList.item(j);
                        String user = reviewElement.getElementsByTagName("user").item(0).getTextContent();
                        String rating = reviewElement.getElementsByTagName("rating").item(0).getTextContent();
                        String comment = reviewElement.getElementsByTagName("comment").item(0).getTextContent();
                        reviews.add(new Review(user, Integer.parseInt(rating), comment));
                    }
                    book.setReviews(reviews);

                    // Парсинг наград
                    NodeList awardsNodeList = bookElement.getElementsByTagName("award");
                    List<String> awards = new ArrayList<>();
                    for (int j = 0; j < awardsNodeList.getLength(); j++) {
                        awards.add(awardsNodeList.item(j).getTextContent());
                    }
                    book.setAwards(awards);

                    // Добавляем книгу в список
                    bookList.add(book);
                }
            }

            // Выводим информацию о книгах
            for (Book book : bookList) {
                System.out.println(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Book {
    private Integer id;
    private String title;
    private String author;
    private Integer year;
    private String genre;
    private Price price = new Price();
    private Publisher publisher;
    private List<Review> reviews = new ArrayList<>();
    private List<String> awards = new ArrayList<>();

    public static class Price {
        private Double value;
        private String currency;

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Price getPrice() {
        return price;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<String> getAwards() {
        return awards;
    }

    public void setAwards(List<String> awards) {
        this.awards = awards;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                ", genre='" + genre + '\'' +
                ", price=" + price.getValue() + " " + price.getCurrency() +
                ", publisher=" + (publisher != null ? publisher : "N/A") +
                ", reviews=" + reviews +
                ", awards=" + awards +
                '}';
    }
}

class Publisher {
    private String name;
    private String city;
    private String country;

    public Publisher(String name, String city, String country) {
        this.name = name;
        this.city = city;
        this.country = country;
    }

    @Override
    public String toString() {
        return name + ", " + city + ", " + country;
    }
}

class Review {
    private String user;
    private Integer rating;
    private String comment;

    public Review(String user, Integer rating, String comment) {
        this.user = user;
        this.rating = rating;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return user + ": " + rating + "/5 - " + comment;
    }
}
