package materialtest.theartistandtheengineer.co.materialtest.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import materialtest.theartistandtheengineer.co.materialtest.logging.L;

/**
 * Created by mpcen-desktop on 3/26/15.
 */
public class Book implements Parcelable{
    private String id;
    private String volumeInfo;
    private String title;
    private String authors;
    private String author;
    private String industryIdentifiers;
    private String type;
    private String identifier;
    private String bcondition;
    private String transaction_status;
    private String imageLinks;
    private String urlThumbnail;
    private String selfLink;
    private String ISBN_13;
    private String isbn;
    //private String condition;
    private String image_url;
    private String seller_id;
    private String repututation_avg;
    private String price;
    private String transactionStatus;
    private String sellerId;
    private String tid;
    private String seller_email;
    private int tid_int;

    public Book(String id,
                String volumeInfo,
                String title,
                String authors,
                String industryIdentifiers,
                String type,
                String identifier,
                String imageLinks,
                String urlThumbnail,
                String selfLink,
                String ISBN_13){

        this.id = id;
        this.volumeInfo = volumeInfo;
        this.title = title;
        this.authors = authors;
        this.industryIdentifiers = industryIdentifiers;
        this.type = type;
        this.identifier = identifier;
        this.imageLinks = imageLinks;
        this.urlThumbnail = urlThumbnail;
        this.selfLink = selfLink;
        this.ISBN_13 = ISBN_13;
    }

    /*
    book.setAuthors(author);
                            book.setImageLinks(image_url);
                            book.setTitle(title);
                            book.setCondition(bcondition);
                            book.setISBN_13(isbn);
                            book.setPrice(price);
                            book.setTransactionStatus(transaction_status);
                            book.seturlThumbnail(image_url);
                            book.setSellerId(seller_id);
                            book.setTid(tid_int);

     */

    public Book(
                String author,
                String title,
                String bcondition,
                String isbn,
                String price,
                String transaction_status,
                String image_url,
                String seller_id,
                String repututation_avg,
                String tid){

        this.author = author;
        this.title = title;
        //this.condition = condition;
        this.isbn = isbn;
        this.price = price;
        this.transaction_status = transaction_status;
        this.image_url = image_url;
        this.seller_id = seller_id;
        this.repututation_avg = repututation_avg;
        this.tid = tid;

    }

    public Book() {

    }

    public Book(Parcel input){
        title = input.readString();
        authors = input.readString();
        ISBN_13 = input.readString();
        urlThumbnail = input.readString();
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getVolumeInfo(){
        return volumeInfo;
    }

    public void setVolumeInfo(String volumeInfo){
        this.volumeInfo = volumeInfo;
    }

    public String getAuthors(){
        return authors;
    }

    public void setAuthors(String authors){
        this.authors = authors;
    }

    public String getIndustryIdentifiers(){
        return industryIdentifiers;
    }

    public void setIndustryIdentifiers(String industryIdentifiers){
        this.industryIdentifiers = industryIdentifiers;
    }

    public String getISBN_13(){
        return ISBN_13;
    }

    public void setISBN_13(String ISBN_13){
        this.ISBN_13 = ISBN_13;
    }

    public String getImageLinks(){
        return imageLinks;
    }

    public void setImageLinks(String imageLinks){
        this.imageLinks = imageLinks;
    }

    public String geturlThumbnail(){
        return urlThumbnail;
    }

    public void seturlThumbnail(String urlThumbnail){
        this.urlThumbnail = urlThumbnail;
    }

    public String getSelfLink(){
        return selfLink;
    }

    public void setSelfLink(String selfLink){
        this.selfLink = selfLink;
    }



    @Override
    public String toString(){
        return "Title: "+title+
                "Author: "+authors+
                "ISBN_13: "+ISBN_13+
                "urlThumbnail "+urlThumbnail+
                "imageLinks "+imageLinks;
    }

    @Override
    public int describeContents() {
        L.m("describe Contents Book");
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        L.m("writeToParcel Book");
        dest.writeString(title);
        dest.writeString(authors);
        dest.writeString(ISBN_13);
        dest.writeString(urlThumbnail);
    }

    public static final Parcelable.Creator<Book> CREATOR
            = new Parcelable.Creator<Book>(){
        public Book createFromParcel(Parcel in){
            L.m("create from parcel: Book");
            return new Book(in);
        }
        public Book[] newArray(int size){
            return new Book[size];
        }
    };


    public void setBcondition(String bcondition) {
        this.bcondition = bcondition;
    }

    public String getBcondition() {
        return bcondition;
    }

    public void setRepututation_avg(String repututation_avg) {
        this.repututation_avg = repututation_avg;
    }

    public String getReputationAvg() {
        return repututation_avg;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setUniqueId(String tid) {
        this.tid = tid;
    }

    public String getUniqueId() {
        return tid;
    }

    public void setSellerEmail(String seller_email) {
        this.seller_email = seller_email;
    }

    public String getSellerEmail() {
        return seller_email;
    }


}