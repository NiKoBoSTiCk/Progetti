CREATE SCHEMA IF NOT EXISTS `myserieslist` DEFAULT CHARACTER SET utf8

CREATE TABLE IF NOT EXISTS `myserieslist`.`actor` (
    `id` INT NOT NULL,
    `firstName` VARCHAR(45) NOT NULL,
    `lastName` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3

CREATE TABLE IF NOT EXISTS `myserieslist`.`cast` (
     `idSeries` INT NOT NULL,
     `idActor` INT NOT NULL,
     PRIMARY KEY (`idSeries`, `idActor`),
    INDEX `fk_Serie_has_Attore_Attore1_idx` (`idActor` ASC) VISIBLE,
    INDEX `fk_Serie_has_Attore_Serie1_idx` (`idSeries` ASC) VISIBLE,
    CONSTRAINT `fk_Serie_has_Attore_Attore1`
    FOREIGN KEY (`idActor`)
    REFERENCES `myserieslist`.`actor` (`id`),
    CONSTRAINT `fk_Serie_has_Attore_Serie1`
    FOREIGN KEY (`idSeries`)
    REFERENCES `myserieslist`.`series` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3

CREATE TABLE IF NOT EXISTS `myserieslist`.`series` (
   `id` INT NOT NULL,
   `name` VARCHAR(45) NOT NULL,
    `episodes` INT NOT NULL,
    `rating` DOUBLE NULL DEFAULT NULL,
    `views` INT NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3

CREATE TABLE IF NOT EXISTS `myserieslist`.`user` (
    `id` INT NOT NULL,
    `firstName` VARCHAR(45) NOT NULL,
    `lastName` VARCHAR(45) NOT NULL,
    `email` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3

CREATE TABLE IF NOT EXISTS `myserieslist`.`watchlist` (
  `idUser` INT NOT NULL,
  `idSeries` INT NOT NULL,
  `progress` INT NOT NULL DEFAULT '0',
  `score` INT NULL DEFAULT NULL,
  `comment` VARCHAR(100) NULL DEFAULT NULL,
    `status` ENUM('WATCHING', 'COMPLETED', 'DROPPED') NULL DEFAULT 'WATCHING',
    `id` INT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_User_has_Serie_Serie1_idx` (`idSeries` ASC) VISIBLE,
    INDEX `fk_User_has_Serie_User_idx` (`idUser` ASC) VISIBLE,
    CONSTRAINT `fk_User_has_Serie_Serie1`
    FOREIGN KEY (`idSeries`)
    REFERENCES `myserieslist`.`series` (`id`),
    CONSTRAINT `fk_User_has_Serie_User`
    FOREIGN KEY (`idUser`)
    REFERENCES `myserieslist`.`user` (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3