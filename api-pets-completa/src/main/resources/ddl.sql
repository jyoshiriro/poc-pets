create table pet (
       id bigint generated by default as identity,
        atualizacao timestamp,
        cpf_dono varchar(255),
        criacao timestamp,
        email_dono varchar(50),
        filhotes integer,
        id_raca bigint,
        nascimento date,
        nome varchar(30),
        nome_dono varchar(255),
        peso double not null,
        telefone_dono varchar(15),
        tipo integer not null,
        primary key (id)
)
