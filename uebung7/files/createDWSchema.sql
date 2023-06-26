-- FROM stores-and-products
CREATE TABLE Country (
  CountryID int NOT NULL,
  Name varchar(255) NOT NULL,
  PRIMARY KEY (CountryID)
);

CREATE TABLE Region (
  RegionID int NOT NULL ,
  CountryID int NOT NULL,
  Name varchar(255) NOT NULL,
  PRIMARY KEY (RegionID)
);

CREATE TABLE City (
  CityID int NOT NULL ,
  RegionID int NOT NULL,
  Name varchar(255) NOT NULL,
  PRIMARY KEY (CityID)
);

CREATE TABLE Shop (
  ShopID int NOT NULL ,
  CityID int NOT NULL,
  Name varchar(255) NOT NULL,
  PRIMARY KEY (ShopID)
);

CREATE TABLE Article (
  ArticleID int NOT NULL ,
  ProductGroupID int NOT NULL,
  Name varchar(255) NOT NULL,
  Price double precision NOT NULL,
  PRIMARY KEY (ArticleID)
);

CREATE TABLE ProductGroup (
  ProductGroupID int NOT NULL ,
  ProductFamilyID int NOT NULL,
  Name varchar(255) NOT NULL,
  PRIMARY KEY (ProductGroupID)
);

CREATE TABLE ProductFamily (
  ProductFamilyID int NOT NULL ,
  ProductCategoryID int NOT NULL,
  Name varchar(255) NOT NULL,
  PRIMARY KEY (ProductFamilyID)
);

CREATE TABLE ProductCategory (
  ProductCategoryID int NOT NULL ,
  Name varchar(255) NOT NULL,
  PRIMARY KEY (ProductCategoryID)
);

ALTER TABLE Shop ADD CONSTRAINT ShopID_fk_1 FOREIGN KEY (CityID) REFERENCES City (CityID);
ALTER TABLE City ADD CONSTRAINT CityID_fk_1 FOREIGN KEY (RegionID) REFERENCES Region (RegionID);
ALTER TABLE Region ADD CONSTRAINT RegionID_fk_1 FOREIGN KEY (CountryID) REFERENCES Country (CountryID);

ALTER TABLE Article ADD CONSTRAINT ArticleID_fk_1 FOREIGN KEY (ProductGroupID) REFERENCES ProductGroup (ProductGroupID);
ALTER TABLE ProductGroup ADD CONSTRAINT ProductGroupID_fk_1 FOREIGN KEY (ProductFamilyID) REFERENCES ProductFamily (ProductFamilyID);
ALTER TABLE ProductFamily ADD CONSTRAINT ProductFamilyID_fk_1 FOREIGN KEY (ProductCategoryID) REFERENCES ProductCategory (ProductCategoryID);

-- additional tables for the sales
CREATE TABLE SALES (
    SalesID int NOT NULL,
    SALE_DATE DATE NOT NULL,
    ShopID int NOT NULL,
    ArticleID int NOT NULL,
    Sells int NOT NULL,
    Revenue MONEY NOT NULL
    PRIMARY KEY (SalesID)
)

ALTER TABLE SALES ADD CONSTRAINT sales_fk_1 FOREIGN KEY (ShopID) references SHOP (ShopID);
ALTER TABLE SALES ADD CONSTRAINT sales_fk_2 FOREIGN KEY (ArticleID) references ARTICLE (ArticleID);