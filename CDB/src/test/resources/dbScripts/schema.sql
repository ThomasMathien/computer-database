 CREATE TABLE IF NOT EXISTS COMPANY(
    `id`                        bigint not null AUTO_INCREMENT,
    `name`                      varchar(255),
     PRIMARY KEY (`id`)
     
 );
 
   CREATE TABLE IF NOT EXISTS COMPUTER (
    `id`                        bigint not null AUTO_INCREMENT,
    `name`                      varchar(255),
    `introduced`                date NULL,
    `discontinued`              date NULL,
    `company_id`                bigint default NULL,
     PRIMARY KEY (`id`),
     foreign key (`company_id`) references COMPANY(`id`)
 );
 