--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.5
-- Dumped by pg_dump version 9.6.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: geocab; Type: SCHEMA; Schema: -; Owner: geocab
--

CREATE SCHEMA geocab;


ALTER SCHEMA geocab OWNER TO geocab;

SET search_path = geocab, pg_catalog;

SET default_tablespace = geocab_dados;

SET default_with_oids = false;

--
-- Name: access_group; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE access_group (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone,
    description character varying(255) NOT NULL,
    name character varying(144) NOT NULL
);


ALTER TABLE access_group OWNER TO geocab;

--
-- Name: access_group_custom_search; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE access_group_custom_search (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone,
    access_group_id bigint,
    custom_search_id bigint
);


ALTER TABLE access_group_custom_search OWNER TO geocab;

--
-- Name: access_group_custom_search_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE access_group_custom_search_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE access_group_custom_search_id_seq OWNER TO geocab;

--
-- Name: access_group_custom_search_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE access_group_custom_search_id_seq OWNED BY access_group_custom_search.id;


--
-- Name: access_group_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE access_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE access_group_id_seq OWNER TO geocab;

--
-- Name: access_group_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE access_group_id_seq OWNED BY access_group.id;


--
-- Name: access_group_layer; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE access_group_layer (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone,
    access_group_id bigint,
    layer_id bigint
);


ALTER TABLE access_group_layer OWNER TO geocab;

--
-- Name: access_group_layer_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE access_group_layer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE access_group_layer_id_seq OWNER TO geocab;

--
-- Name: access_group_layer_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE access_group_layer_id_seq OWNED BY access_group_layer.id;


--
-- Name: access_group_tool; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE access_group_tool (
    access_group_id bigint NOT NULL,
    tool_id bigint NOT NULL
);


ALTER TABLE access_group_tool OWNER TO geocab;

--
-- Name: access_group_tool_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE access_group_tool_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE access_group_tool_id_seq OWNER TO geocab;

--
-- Name: access_group_user; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE access_group_user (
    access_group_id bigint NOT NULL,
    user_username bigint NOT NULL
);


ALTER TABLE access_group_user OWNER TO geocab;

--
-- Name: access_group_user_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE access_group_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE access_group_user_id_seq OWNER TO geocab;

--
-- Name: attribute; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE attribute (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone,
    attribute_default boolean,
    name character varying(255) NOT NULL,
    required boolean NOT NULL,
    type integer NOT NULL,
    layer_id bigint,
    order_attribute integer,
    visible boolean DEFAULT true NOT NULL
);


ALTER TABLE attribute OWNER TO geocab;

--
-- Name: attribute_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE attribute_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE attribute_id_seq OWNER TO geocab;

--
-- Name: attribute_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE attribute_id_seq OWNED BY attribute.id;


--
-- Name: attribute_option; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE attribute_option (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone,
    attribute_id bigint,
    description character varying(50) NOT NULL
);


ALTER TABLE attribute_option OWNER TO geocab;

--
-- Name: attribute_option_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE attribute_option_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE attribute_option_id_seq OWNER TO geocab;

--
-- Name: attribute_option_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE attribute_option_id_seq OWNED BY attribute_option.id;


--
-- Name: configuration; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE configuration (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone,
    background_map integer NOT NULL,
    stop_send_email boolean NOT NULL
);


ALTER TABLE configuration OWNER TO geocab;

--
-- Name: configuration_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE configuration_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE configuration_id_seq OWNER TO geocab;

--
-- Name: configuration_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE configuration_id_seq OWNED BY configuration.id;


--
-- Name: custom_search; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE custom_search (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone,
    name character varying(144) NOT NULL,
    layer_id bigint NOT NULL
);


ALTER TABLE custom_search OWNER TO geocab;

--
-- Name: custom_search_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE custom_search_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE custom_search_id_seq OWNER TO geocab;

--
-- Name: custom_search_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE custom_search_id_seq OWNED BY custom_search.id;


--
-- Name: data_source; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE data_source (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone,
    login character varying(144),
    name character varying(144) NOT NULL,
    password character varying(144),
    url character varying(255)
);


ALTER TABLE data_source OWNER TO geocab;

--
-- Name: data_source_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE data_source_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE data_source_id_seq OWNER TO geocab;

--
-- Name: data_source_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE data_source_id_seq OWNED BY data_source.id;


--
-- Name: field_layer; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE field_layer (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone,
    label character varying(144),
    nome character varying(144) NOT NULL,
    order_campo_camada integer,
    tipo integer NOT NULL
);


ALTER TABLE field_layer OWNER TO geocab;

--
-- Name: field_layer_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE field_layer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE field_layer_id_seq OWNER TO geocab;

--
-- Name: field_layer_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE field_layer_id_seq OWNED BY field_layer.id;


--
-- Name: layer; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE layer (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone,
    enabled boolean,
    icon character varying(255),
    maximum_scale_map integer NOT NULL,
    minimum_scale_map integer NOT NULL,
    name character varying(144) NOT NULL,
    order_layer integer,
    published boolean,
    start_enabled boolean NOT NULL,
    start_visible boolean NOT NULL,
    title character varying(144) NOT NULL,
    data_source_id bigint,
    layer_group_id bigint,
    published_layer_id bigint
);


ALTER TABLE layer OWNER TO geocab;

--
-- Name: layer_field; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE layer_field (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone,
    label character varying(144),
    name character varying(144) NOT NULL,
    order_layer integer,
    type integer NOT NULL,
    pesquisa_id bigint,
    attribute_id bigint
);


ALTER TABLE layer_field OWNER TO geocab;

--
-- Name: layer_field_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE layer_field_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE layer_field_id_seq OWNER TO geocab;

--
-- Name: layer_field_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE layer_field_id_seq OWNED BY layer_field.id;


--
-- Name: layer_group; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE layer_group (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone,
    name character varying(144) NOT NULL,
    order_layer_group integer,
    published boolean,
    draft_id bigint,
    layer_group_upper_id bigint
);


ALTER TABLE layer_group OWNER TO geocab;

--
-- Name: layer_group_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE layer_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE layer_group_id_seq OWNER TO geocab;

--
-- Name: layer_group_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE layer_group_id_seq OWNED BY layer_group.id;


--
-- Name: layer_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE layer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE layer_id_seq OWNER TO geocab;

--
-- Name: layer_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE layer_id_seq OWNED BY layer.id;


--
-- Name: marker; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE marker (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone,
    deleted boolean,
    status integer NOT NULL,
    layer_id bigint,
    user_id bigint,
    location postgis.geometry
);


ALTER TABLE marker OWNER TO geocab;

--
-- Name: marker_attribute; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE marker_attribute (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone,
    value character varying(255) NOT NULL,
    attribute_id bigint,
    marker_id bigint,
    photo_album_id bigint,
    selected_attribute_id bigint
);


ALTER TABLE marker_attribute OWNER TO geocab;

--
-- Name: marker_attribute_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE marker_attribute_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE marker_attribute_id_seq OWNER TO geocab;

--
-- Name: marker_attribute_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE marker_attribute_id_seq OWNED BY marker_attribute.id;


--
-- Name: marker_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE marker_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE marker_id_seq OWNER TO geocab;

--
-- Name: marker_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE marker_id_seq OWNED BY marker.id;


--
-- Name: marker_moderation; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE marker_moderation (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone,
    marker_id bigint NOT NULL,
    status integer NOT NULL
);


ALTER TABLE marker_moderation OWNER TO geocab;

--
-- Name: marker_moderation_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE marker_moderation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE marker_moderation_id_seq OWNER TO geocab;

--
-- Name: marker_moderation_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE marker_moderation_id_seq OWNED BY marker_moderation.id;


--
-- Name: motive; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE motive (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone,
    name character varying(255) NOT NULL
);


ALTER TABLE motive OWNER TO geocab;

--
-- Name: motive_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE motive_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE motive_id_seq OWNER TO geocab;

--
-- Name: motive_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE motive_id_seq OWNED BY motive.id;


--
-- Name: motive_marker_moderation; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE motive_marker_moderation (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone,
    marker_moderation_id bigint,
    motive_id bigint,
    description character varying(255) NOT NULL
);


ALTER TABLE motive_marker_moderation OWNER TO geocab;

--
-- Name: motive_marker_moderation_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE motive_marker_moderation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE motive_marker_moderation_id_seq OWNER TO geocab;

--
-- Name: motive_marker_moderation_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE motive_marker_moderation_id_seq OWNED BY motive_marker_moderation.id;


--
-- Name: photo; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE photo (
    id bigint NOT NULL,
    description character varying(60) NOT NULL,
    identifier character varying(50),
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone,
    photo_album_id bigint
);


ALTER TABLE photo OWNER TO geocab;

--
-- Name: photo_album; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE photo_album (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone,
    identifier character varying(50),
    marker_attribute_id bigint
);


ALTER TABLE photo_album OWNER TO geocab;

--
-- Name: photo_album_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE photo_album_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE photo_album_id_seq OWNER TO geocab;

--
-- Name: photo_album_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE photo_album_id_seq OWNED BY photo_album.id;


--
-- Name: photo_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE photo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE photo_id_seq OWNER TO geocab;

--
-- Name: photo_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE photo_id_seq OWNED BY photo.id;


--
-- Name: revision; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE revision (
    id bigint NOT NULL,
    "timestamp" bigint NOT NULL,
    user_id bigint
);


ALTER TABLE revision OWNER TO geocab;

--
-- Name: revision_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE revision_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE revision_id_seq OWNER TO geocab;

--
-- Name: revision_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE revision_id_seq OWNED BY revision.id;


--
-- Name: tool; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE tool (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone,
    description character varying(255) NOT NULL,
    name character varying(144) NOT NULL
);


ALTER TABLE tool OWNER TO geocab;

--
-- Name: tool_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE tool_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tool_id_seq OWNER TO geocab;

--
-- Name: tool_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE tool_id_seq OWNED BY tool.id;


--
-- Name: user; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE "user" (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone,
    email character varying(144) NOT NULL,
    enabled boolean NOT NULL,
    name character varying(50) NOT NULL,
    password character varying(100) NOT NULL,
    role integer NOT NULL,
    coordinates integer DEFAULT 1 NOT NULL,
    background_map integer DEFAULT 6 NOT NULL
);


ALTER TABLE "user" OWNER TO geocab;

--
-- Name: user_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE user_id_seq OWNER TO geocab;

--
-- Name: user_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE user_id_seq OWNED BY "user".id;


--
-- Name: user_social_connection; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE user_social_connection (
    id bigint NOT NULL,
    access_token character varying(255) NOT NULL,
    display_name character varying(255),
    expire_time bigint,
    image_url character varying(512),
    profile_url character varying(512),
    provider_id character varying(255) NOT NULL,
    provider_user_id character varying(255),
    rank integer NOT NULL,
    refresh_token character varying(255),
    secret character varying(255),
    user_id bigint NOT NULL
);


ALTER TABLE user_social_connection OWNER TO geocab;

--
-- Name: user_social_connection_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE user_social_connection_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE user_social_connection_id_seq OWNER TO geocab;

--
-- Name: user_social_connection_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE user_social_connection_id_seq OWNED BY user_social_connection.id;


--
-- Name: access_group id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY access_group ALTER COLUMN id SET DEFAULT nextval('access_group_id_seq'::regclass);


--
-- Name: access_group_custom_search id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY access_group_custom_search ALTER COLUMN id SET DEFAULT nextval('access_group_custom_search_id_seq'::regclass);


--
-- Name: access_group_layer id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY access_group_layer ALTER COLUMN id SET DEFAULT nextval('access_group_layer_id_seq'::regclass);


--
-- Name: attribute id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY attribute ALTER COLUMN id SET DEFAULT nextval('attribute_id_seq'::regclass);


--
-- Name: attribute_option id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY attribute_option ALTER COLUMN id SET DEFAULT nextval('attribute_option_id_seq'::regclass);


--
-- Name: configuration id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY configuration ALTER COLUMN id SET DEFAULT nextval('configuration_id_seq'::regclass);


--
-- Name: custom_search id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY custom_search ALTER COLUMN id SET DEFAULT nextval('custom_search_id_seq'::regclass);


--
-- Name: data_source id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY data_source ALTER COLUMN id SET DEFAULT nextval('data_source_id_seq'::regclass);


--
-- Name: field_layer id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY field_layer ALTER COLUMN id SET DEFAULT nextval('field_layer_id_seq'::regclass);


--
-- Name: layer id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY layer ALTER COLUMN id SET DEFAULT nextval('layer_id_seq'::regclass);


--
-- Name: layer_field id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY layer_field ALTER COLUMN id SET DEFAULT nextval('layer_field_id_seq'::regclass);


--
-- Name: layer_group id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY layer_group ALTER COLUMN id SET DEFAULT nextval('layer_group_id_seq'::regclass);


--
-- Name: marker id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY marker ALTER COLUMN id SET DEFAULT nextval('marker_id_seq'::regclass);


--
-- Name: marker_attribute id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY marker_attribute ALTER COLUMN id SET DEFAULT nextval('marker_attribute_id_seq'::regclass);


--
-- Name: marker_moderation id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY marker_moderation ALTER COLUMN id SET DEFAULT nextval('marker_moderation_id_seq'::regclass);


--
-- Name: motive id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY motive ALTER COLUMN id SET DEFAULT nextval('motive_id_seq'::regclass);


--
-- Name: motive_marker_moderation id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY motive_marker_moderation ALTER COLUMN id SET DEFAULT nextval('motive_marker_moderation_id_seq'::regclass);


--
-- Name: photo id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY photo ALTER COLUMN id SET DEFAULT nextval('photo_id_seq'::regclass);


--
-- Name: photo_album id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY photo_album ALTER COLUMN id SET DEFAULT nextval('photo_album_id_seq'::regclass);


--
-- Name: revision id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY revision ALTER COLUMN id SET DEFAULT nextval('revision_id_seq'::regclass);


--
-- Name: tool id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY tool ALTER COLUMN id SET DEFAULT nextval('tool_id_seq'::regclass);


--
-- Name: user id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY "user" ALTER COLUMN id SET DEFAULT nextval('user_id_seq'::regclass);


--
-- Name: user_social_connection id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY user_social_connection ALTER COLUMN id SET DEFAULT nextval('user_social_connection_id_seq'::regclass);


SET default_tablespace = geocab_indices;

--
-- Name: access_group_custom_search access_group_custom_search_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY access_group_custom_search
    ADD CONSTRAINT access_group_custom_search_pkey PRIMARY KEY (id);


--
-- Name: access_group_layer access_group_layer_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY access_group_layer
    ADD CONSTRAINT access_group_layer_pkey PRIMARY KEY (id);


--
-- Name: access_group access_group_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY access_group
    ADD CONSTRAINT access_group_pkey PRIMARY KEY (id);


--
-- Name: access_group_tool access_group_tool_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY access_group_tool
    ADD CONSTRAINT access_group_tool_pkey PRIMARY KEY (access_group_id, tool_id);


--
-- Name: access_group_user access_group_user_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY access_group_user
    ADD CONSTRAINT access_group_user_pkey PRIMARY KEY (access_group_id, user_username);


--
-- Name: attribute_option attribute_option_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY attribute_option
    ADD CONSTRAINT attribute_option_pkey PRIMARY KEY (id);


--
-- Name: attribute attribute_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY attribute
    ADD CONSTRAINT attribute_pkey PRIMARY KEY (id);


--
-- Name: configuration configuration_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY configuration
    ADD CONSTRAINT configuration_pkey PRIMARY KEY (id);


--
-- Name: custom_search custom_search_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY custom_search
    ADD CONSTRAINT custom_search_pkey PRIMARY KEY (id);


--
-- Name: data_source data_source_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY data_source
    ADD CONSTRAINT data_source_pkey PRIMARY KEY (id);


--
-- Name: field_layer field_layer_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY field_layer
    ADD CONSTRAINT field_layer_pkey PRIMARY KEY (id);


--
-- Name: layer_field layer_field_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY layer_field
    ADD CONSTRAINT layer_field_pkey PRIMARY KEY (id);


--
-- Name: layer_group layer_group_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY layer_group
    ADD CONSTRAINT layer_group_pkey PRIMARY KEY (id);


--
-- Name: layer layer_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY layer
    ADD CONSTRAINT layer_pkey PRIMARY KEY (id);


--
-- Name: marker_attribute marker_attribute_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY marker_attribute
    ADD CONSTRAINT marker_attribute_pkey PRIMARY KEY (id);


--
-- Name: marker_moderation marker_moderation_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY marker_moderation
    ADD CONSTRAINT marker_moderation_pkey PRIMARY KEY (id);


--
-- Name: marker marker_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY marker
    ADD CONSTRAINT marker_pkey PRIMARY KEY (id);


--
-- Name: motive_marker_moderation motive_marker_moderation_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY motive_marker_moderation
    ADD CONSTRAINT motive_marker_moderation_pkey PRIMARY KEY (id);


--
-- Name: motive motive_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY motive
    ADD CONSTRAINT motive_pkey PRIMARY KEY (id);


--
-- Name: photo_album photo_album_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY photo_album
    ADD CONSTRAINT photo_album_pkey PRIMARY KEY (id);


--
-- Name: photo photo_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY photo
    ADD CONSTRAINT photo_pkey PRIMARY KEY (id);


--
-- Name: revision revision_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY revision
    ADD CONSTRAINT revision_pkey PRIMARY KEY (id);


--
-- Name: tool tool_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY tool
    ADD CONSTRAINT tool_pkey PRIMARY KEY (id);


--
-- Name: access_group uk_access_group_name; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY access_group
    ADD CONSTRAINT uk_access_group_name UNIQUE (name);


--
-- Name: custom_search uk_custom_search_name; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY custom_search
    ADD CONSTRAINT uk_custom_search_name UNIQUE (name);


--
-- Name: data_source uk_data_source_name; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY data_source
    ADD CONSTRAINT uk_data_source_name UNIQUE (name);


--
-- Name: layer_group uk_layer_group_layer_group_upper_id_name; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY layer_group
    ADD CONSTRAINT uk_layer_group_layer_group_upper_id_name UNIQUE (name, layer_group_upper_id);


--
-- Name: photo_album uk_photo_album_identifier; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY photo_album
    ADD CONSTRAINT uk_photo_album_identifier UNIQUE (identifier);


--
-- Name: photo_album uk_photo_album_marker_attribute_id; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY photo_album
    ADD CONSTRAINT uk_photo_album_marker_attribute_id UNIQUE (marker_attribute_id);


--
-- Name: tool uk_tool_name; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY tool
    ADD CONSTRAINT uk_tool_name UNIQUE (name);


--
-- Name: user uk_user_email; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT uk_user_email UNIQUE (email);


--
-- Name: user_social_connection uk_user_social_connection_provider_id_rank_user_id; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY user_social_connection
    ADD CONSTRAINT uk_user_social_connection_provider_id_rank_user_id UNIQUE (user_id, provider_id, rank);


--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: user_social_connection user_social_connection_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY user_social_connection
    ADD CONSTRAINT user_social_connection_pkey PRIMARY KEY (id);


--
-- Name: marker_location_idx; Type: INDEX; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

CREATE INDEX marker_location_idx ON marker USING gist (location);


--
-- Name: access_group_custom_search fk_access_group_custom_search_access_group_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY access_group_custom_search
    ADD CONSTRAINT fk_access_group_custom_search_access_group_id FOREIGN KEY (access_group_id) REFERENCES access_group(id);


--
-- Name: access_group_custom_search fk_access_group_custom_search_custom_search_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY access_group_custom_search
    ADD CONSTRAINT fk_access_group_custom_search_custom_search_id FOREIGN KEY (custom_search_id) REFERENCES custom_search(id);


--
-- Name: access_group_layer fk_access_group_layer_access_group_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY access_group_layer
    ADD CONSTRAINT fk_access_group_layer_access_group_id FOREIGN KEY (access_group_id) REFERENCES access_group(id);


--
-- Name: access_group_layer fk_access_group_layer_layer_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY access_group_layer
    ADD CONSTRAINT fk_access_group_layer_layer_id FOREIGN KEY (layer_id) REFERENCES layer(id);


--
-- Name: access_group_tool fk_access_group_tool_access_group_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY access_group_tool
    ADD CONSTRAINT fk_access_group_tool_access_group_id FOREIGN KEY (access_group_id) REFERENCES access_group(id);


--
-- Name: access_group_tool fk_access_group_tool_tool_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY access_group_tool
    ADD CONSTRAINT fk_access_group_tool_tool_id FOREIGN KEY (tool_id) REFERENCES tool(id);


--
-- Name: access_group_user fk_access_group_user_access_group_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY access_group_user
    ADD CONSTRAINT fk_access_group_user_access_group_id FOREIGN KEY (access_group_id) REFERENCES access_group(id);


--
-- Name: access_group_user fk_access_group_user_user_username; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY access_group_user
    ADD CONSTRAINT fk_access_group_user_user_username FOREIGN KEY (user_username) REFERENCES "user"(id);


--
-- Name: attribute fk_attribute_layer_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY attribute
    ADD CONSTRAINT fk_attribute_layer_id FOREIGN KEY (layer_id) REFERENCES layer(id);


--
-- Name: attribute_option fk_attribute_option_attribute_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY attribute_option
    ADD CONSTRAINT fk_attribute_option_attribute_id FOREIGN KEY (attribute_id) REFERENCES attribute(id);


--
-- Name: custom_search fk_custom_search_layer_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY custom_search
    ADD CONSTRAINT fk_custom_search_layer_id FOREIGN KEY (layer_id) REFERENCES layer(id);


--
-- Name: layer fk_layer_data_source_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY layer
    ADD CONSTRAINT fk_layer_data_source_id FOREIGN KEY (data_source_id) REFERENCES data_source(id);


--
-- Name: layer_field fk_layer_field_pesquisa_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY layer_field
    ADD CONSTRAINT fk_layer_field_pesquisa_id FOREIGN KEY (pesquisa_id) REFERENCES custom_search(id);


--
-- Name: layer_group fk_layer_group_draft_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY layer_group
    ADD CONSTRAINT fk_layer_group_draft_id FOREIGN KEY (draft_id) REFERENCES layer_group(id);


--
-- Name: layer_group fk_layer_group_layer_group_upper_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY layer_group
    ADD CONSTRAINT fk_layer_group_layer_group_upper_id FOREIGN KEY (layer_group_upper_id) REFERENCES layer_group(id);


--
-- Name: layer fk_layer_layer_group_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY layer
    ADD CONSTRAINT fk_layer_layer_group_id FOREIGN KEY (layer_group_id) REFERENCES layer_group(id);


--
-- Name: layer fk_layer_published_layer_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY layer
    ADD CONSTRAINT fk_layer_published_layer_id FOREIGN KEY (published_layer_id) REFERENCES layer(id);


--
-- Name: marker_attribute fk_marker_attribute_attribute_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY marker_attribute
    ADD CONSTRAINT fk_marker_attribute_attribute_id FOREIGN KEY (attribute_id) REFERENCES attribute(id);


--
-- Name: marker_attribute fk_marker_attribute_marker_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY marker_attribute
    ADD CONSTRAINT fk_marker_attribute_marker_id FOREIGN KEY (marker_id) REFERENCES marker(id);


--
-- Name: marker_attribute fk_marker_attribute_photo_album_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY marker_attribute
    ADD CONSTRAINT fk_marker_attribute_photo_album_id FOREIGN KEY (photo_album_id) REFERENCES photo_album(id);


--
-- Name: marker_attribute fk_marker_attribute_selected_attribute_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY marker_attribute
    ADD CONSTRAINT fk_marker_attribute_selected_attribute_id FOREIGN KEY (selected_attribute_id) REFERENCES attribute_option(id);


--
-- Name: marker fk_marker_layer_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY marker
    ADD CONSTRAINT fk_marker_layer_id FOREIGN KEY (layer_id) REFERENCES layer(id);


--
-- Name: marker_moderation fk_marker_moderation_marker_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY marker_moderation
    ADD CONSTRAINT fk_marker_moderation_marker_id FOREIGN KEY (marker_id) REFERENCES marker(id);


--
-- Name: marker fk_marker_user_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY marker
    ADD CONSTRAINT fk_marker_user_id FOREIGN KEY (user_id) REFERENCES "user"(id);


--
-- Name: motive_marker_moderation fk_motive_marker_moderation_marker_moderation_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY motive_marker_moderation
    ADD CONSTRAINT fk_motive_marker_moderation_marker_moderation_id FOREIGN KEY (marker_moderation_id) REFERENCES marker_moderation(id);


--
-- Name: motive_marker_moderation fk_motive_marker_moderation_motive_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY motive_marker_moderation
    ADD CONSTRAINT fk_motive_marker_moderation_motive_id FOREIGN KEY (motive_id) REFERENCES motive(id);


--
-- Name: photo_album fk_photo_album_marker_attribute_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY photo_album
    ADD CONSTRAINT fk_photo_album_marker_attribute_id FOREIGN KEY (marker_attribute_id) REFERENCES marker_attribute(id);


--
-- Name: photo fk_photo_photo_album_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY photo
    ADD CONSTRAINT fk_photo_photo_album_id FOREIGN KEY (photo_album_id) REFERENCES photo_album(id);


--
-- Name: user_social_connection fk_user_social_connection_user_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY user_social_connection
    ADD CONSTRAINT fk_user_social_connection_user_id FOREIGN KEY (user_id) REFERENCES "user"(id);


--
-- Name: geocab; Type: ACL; Schema: -; Owner: geocab
--

GRANT USAGE ON SCHEMA geocab TO gr_geocab_acesso;


--
-- Name: access_group; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE access_group TO gr_geocab_acesso;


--
-- Name: access_group_custom_search; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE access_group_custom_search TO gr_geocab_acesso;


--
-- Name: access_group_custom_search_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE access_group_custom_search_id_seq TO gr_geocab_acesso;


--
-- Name: access_group_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE access_group_id_seq TO gr_geocab_acesso;


--
-- Name: access_group_layer; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE access_group_layer TO gr_geocab_acesso;


--
-- Name: access_group_layer_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE access_group_layer_id_seq TO gr_geocab_acesso;


--
-- Name: access_group_tool; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE access_group_tool TO gr_geocab_acesso;


--
-- Name: access_group_tool_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE access_group_tool_id_seq TO gr_geocab_acesso;


--
-- Name: access_group_user; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE access_group_user TO gr_geocab_acesso;


--
-- Name: access_group_user_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE access_group_user_id_seq TO gr_geocab_acesso;


--
-- Name: attribute; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE attribute TO gr_geocab_acesso;


--
-- Name: attribute_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE attribute_id_seq TO gr_geocab_acesso;


--
-- Name: attribute_option; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE attribute_option TO gr_geocab_acesso;


--
-- Name: attribute_option_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE attribute_option_id_seq TO gr_geocab_acesso;


--
-- Name: configuration; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE configuration TO gr_geocab_acesso;


--
-- Name: configuration_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE configuration_id_seq TO gr_geocab_acesso;


--
-- Name: custom_search; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE custom_search TO gr_geocab_acesso;


--
-- Name: custom_search_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE custom_search_id_seq TO gr_geocab_acesso;


--
-- Name: data_source; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE data_source TO gr_geocab_acesso;


--
-- Name: data_source_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE data_source_id_seq TO gr_geocab_acesso;


--
-- Name: field_layer; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE field_layer TO gr_geocab_acesso;


--
-- Name: field_layer_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE field_layer_id_seq TO gr_geocab_acesso;


--
-- Name: layer; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE layer TO gr_geocab_acesso;


--
-- Name: layer_field; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE layer_field TO gr_geocab_acesso;


--
-- Name: layer_field_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE layer_field_id_seq TO gr_geocab_acesso;


--
-- Name: layer_group; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE layer_group TO gr_geocab_acesso;


--
-- Name: layer_group_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE layer_group_id_seq TO gr_geocab_acesso;


--
-- Name: layer_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE layer_id_seq TO gr_geocab_acesso;


--
-- Name: marker; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE marker TO gr_geocab_acesso;


--
-- Name: marker_attribute; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE marker_attribute TO gr_geocab_acesso;


--
-- Name: marker_attribute_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE marker_attribute_id_seq TO gr_geocab_acesso;


--
-- Name: marker_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE marker_id_seq TO gr_geocab_acesso;


--
-- Name: marker_moderation; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE marker_moderation TO gr_geocab_acesso;


--
-- Name: marker_moderation_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE marker_moderation_id_seq TO gr_geocab_acesso;


--
-- Name: motive; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE motive TO gr_geocab_acesso;


--
-- Name: motive_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE motive_id_seq TO gr_geocab_acesso;


--
-- Name: motive_marker_moderation; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE motive_marker_moderation TO gr_geocab_acesso;


--
-- Name: motive_marker_moderation_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE motive_marker_moderation_id_seq TO gr_geocab_acesso;


--
-- Name: photo; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE photo TO gr_geocab_acesso;


--
-- Name: photo_album; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE photo_album TO gr_geocab_acesso;


--
-- Name: photo_album_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE photo_album_id_seq TO gr_geocab_acesso;


--
-- Name: photo_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE photo_id_seq TO gr_geocab_acesso;


--
-- Name: revision; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE revision TO gr_geocab_acesso;


--
-- Name: revision_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE revision_id_seq TO gr_geocab_acesso;


--
-- Name: tool; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE tool TO gr_geocab_acesso;


--
-- Name: tool_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE tool_id_seq TO gr_geocab_acesso;


--
-- Name: user; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE "user" TO gr_geocab_acesso;


--
-- Name: user_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE user_id_seq TO gr_geocab_acesso;


--
-- Name: user_social_connection; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE user_social_connection TO gr_geocab_acesso;


--
-- Name: user_social_connection_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

GRANT ALL ON SEQUENCE user_social_connection_id_seq TO gr_geocab_acesso;


--
-- PostgreSQL database dump complete
--

