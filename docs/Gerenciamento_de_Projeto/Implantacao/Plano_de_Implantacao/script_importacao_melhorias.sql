-- Alteração na tabela 'geocab.user' para inserção de coordenadas padrão para usuários:
ALTER TABLE geocab.user ADD COLUMN coordinates INTEGER DEFAULT 1 NOT NULL;
-- Alteração na tabela 'geocab.user' para inserção de plano de fundo padrão para usuários:
ALTER TABLE geocab.user ADD COLUMN background_map INTEGER DEFAULT 6 NOT NULL;
-- Criação da tabela 'geocab.photo_album' para armazenamento de álbum de fotos e alteração na tabela 'marker_attribute' para criação de chave estrangeira de álbum de fotos:
CREATE TABLE geocab.photo_album ( id bigserial NOT NULL, created timestamp
without time zone NOT NULL, updated timestamp without time zone, identifier
character varying(50), marker_attribute_id bigint, CONSTRAINT
photo_album_pkey PRIMARY KEY (id), CONSTRAINT
fk_photo_album_marker_attribute_id FOREIGN KEY (marker_attribute_id)
REFERENCES geocab.marker_attribute (id) MATCH SIMPLE ON UPDATE NO
ACTION ON DELETE NO ACTION, CONSTRAINT uk_photo_album_identifier
UNIQUE (identifier), CONSTRAINT uk_photo_album_marker_attribute_id UNIQUE
(marker_attribute_id));
ALTER TABLE geocab.photo_album OWNER TO geocab;
ALTER TABLE geocab.marker_attribute ADD photo_album_id bigint;
ALTER TABLE geocab.marker_attribute ADD CONSTRAINT
fk_marker_attribute_photo_album_id FOREIGN KEY (photo_album_id)
REFERENCES geocab.photo_album (id) MATCH SIMPLE
ON UPDATE NO
ACTION ON DELETE NO ACTION;
-- Alteração na tabela ‘geocab.attribute’ para inserção da coluna ‘visible’, responsável por tornar o atributo visível ou não no mapa principal da aplicação:
ALTER TABLE geocab.attribute ADD COLUMN visible BOOLEAN DEFAULT TRUE
NOT NULL;
-- Criação da tabela 'geocab.photo' para armazenamento de dados sobre as fotos (não armazena a foto em si):
CREATE TABLE geocab.photo ( id bigserial NOT NULL, description VARCHAR
(60) NOT NULL, identifier VARCHAR (50), created timestamp without time zone
NOT NULL, updated timestamp without time zone, photo_album_id bigint,
CONSTRAINT photo_pkey PRIMARY KEY (id), CONSTRAINT
fk_photo_photo_album_id FOREIGN KEY (photo_album_id) REFERENCES
geocab.photo_album (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE
NO ACTION );
ALTER TABLE geocab.photo OWNER TO geocab;
-- Alteração na tabela ‘geocab.tool’ para inserção da ferramenta de exportação e importação para o formato shapefile:
INSERT INTO geocab.tool (id, created, description, name) VALUES (4, '2014-01-20 00:00:00', 'Ferramenta para SHP', 'Habilitar SHP');
-- Alteração na tabela ‘geocab_auditoria.user_audited’ para criação da coluna 'coordinates' e auditoria da tabela 'geocab.user':
ALTER TABLE geocab_auditoria.user_audited ADD COLUMN coordinates integer;
-- Alteração na tabela ‘geocab_auditoria.user_audited’ para criação da coluna 'background_map' e auditoria da tabela 'geocab.user':
ALTER TABLE geocab_auditoria.user_audited ADD COLUMN background_map integer;
-- Criação da tabela 'geocab_auditoria.photo_album_audited' para auditoria da tabela 'geocab.photo_album':
CREATE TABLE geocab_auditoria.photo_album_audited ( id bigint NOT NULL,
revision bigint NOT NULL, revision_type smallint, identifier character varying(50),
marker_attribute_id bigint, CONSTRAINT photo_album_audited_pkey PRIMARY
KEY (id, revision), CONSTRAINT fk_photo_album_audited_revision FOREIGN
KEY (revision) REFERENCES geocab_auditoria.revision (id) MATCH SIMPLE ON
UPDATE NO ACTION ON DELETE NO ACTION );
ALTER TABLE geocab_auditoria.photo_album_audited OWNER TO geocab_auditoria;
ALTER TABLE geocab_auditoria.marker_attribute_audited ADD COLUMN photo_album_id bigint;
-- Alteração na tabela ‘geocab_auditoria.attribute_audited’ para inserção da coluna ‘visible’ e auditoria da mesma:
ALTER TABLE geocab_auditoria.attribute_audited ADD COLUMN visible BOOLEAN;
-- Criação da tabela 'geocab_auditoria.photo_audited' para auditoria de dados sobre as fotos:
CREATE TABLE geocab_auditoria.photo_audited ( id bigint NOT NULL, revision
bigint NOT NULL, revision_type smallint, identifier character varying(50),
photo_album_id bigint, description character varying(60), CONSTRAINT
photo_audited_pkey PRIMARY KEY (id, revision), CONSTRAINT
fk_photo_audited_revision FOREIGN KEY (revision) REFERENCES
geocab_auditoria.revision (id) MATCH SIMPLE ON UPDATE NO ACTION ON
DELETE NO ACTION );
ALTER TABLE geocab_auditoria.photo_audited OWNER TO geocab_auditoria;
-- Alteração do squema de dados da tabela revision
ALTER TABLE geocab_auditoria.revision SET SCHEMA geocab;

-- Table: geocab.configuration

-- DROP TABLE geocab.configuration;

CREATE TABLE geocab.configuration
(
  id bigserial NOT NULL,
  created timestamp without time zone NOT NULL,
  updated timestamp without time zone,
  background_map integer NOT NULL,
  stop_send_email boolean NOT NULL,
  CONSTRAINT configuration_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE geocab.configuration
  OWNER TO geocab;

-- Table: geocab_auditoria.configuration_audited

-- DROP TABLE geocab_auditoria.configuration_audited;

CREATE TABLE geocab_auditoria.configuration_audited
(
  id bigint NOT NULL,
  revision bigint NOT NULL,
  revision_type smallint,
  background_map integer,
  stop_send_email boolean,
  CONSTRAINT configuration_audited_pkey PRIMARY KEY (id, revision)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE geocab_auditoria.configuration_audited
  OWNER TO geocab;
GRANT ALL ON TABLE geocab_auditoria.configuration_audited TO public;
GRANT ALL ON TABLE geocab_auditoria.configuration_audited TO geocab;

