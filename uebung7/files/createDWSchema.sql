DROP TABLE IF EXISTS Geo, Time, Product, Sales;

CREATE TABLE Geo (
  ShopID int NOT NULL,
  ShopName varchar(255) NOT NULL,
  CityName varchar(255) NOT NULL,
  RegionName varchar(255) NOT NULL,
  CountryName varchar(255) NOT NULL,
  PRIMARY KEY (ShopID)
);

CREATE TABLE Time (
  Date date NOT NULL,
  Day int NOT NULL,
  Month int NOT NULL,
  Quarter int NOT NULL,
  Year int NOT NULL,
  PRIMARY KEY (date)
);

CREATE TABLE Product (
  ArticleID int NOT NULL,
  ArticleName varchar(255) NOT NULL,
  ProductGroupName varchar(255) NOT NULL,
  ProductFamilyName varchar(255) NOT NULL,
  ProductCategoryName varchar(255) NOT NULL,
  Price double precision NOT NULL,
  PRIMARY KEY (ArticleID)
);

CREATE TABLE Sales (
  date date NOT NULL,
  ShopID int NOT NULL,
  ArticleID int NOT NULL,
  Sells int NOT NULL,
  Revenue DECIMAL NOT NULL,
  PRIMARY KEY (date, ShopID, ArticleID),
  FOREIGN KEY (date) REFERENCES Time (date),
  FOREIGN KEY (ShopID) REFERENCES Geo (ShopID),
  FOREIGN KEY (ArticleID) REFERENCES Product (ArticleID)
);
