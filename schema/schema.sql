--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: geocab; Type: SCHEMA; Schema: -; Owner: geocab
--

CREATE SCHEMA geocab;


ALTER SCHEMA geocab OWNER TO geocab;

--
-- Name: geocab_auditoria; Type: SCHEMA; Schema: -; Owner: geocab_auditoria
--

CREATE SCHEMA geocab_auditoria;


ALTER SCHEMA geocab_auditoria OWNER TO geocab_auditoria;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- Name: postgis; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;


--
-- Name: EXTENSION postgis; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis IS 'PostGIS geometry, geography, and raster spatial types and functions';


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


ALTER TABLE geocab.access_group OWNER TO geocab;

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


ALTER TABLE geocab.access_group_custom_search OWNER TO geocab;

--
-- Name: access_group_custom_search_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE access_group_custom_search_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE geocab.access_group_custom_search_id_seq OWNER TO geocab;

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


ALTER TABLE geocab.access_group_id_seq OWNER TO geocab;

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


ALTER TABLE geocab.access_group_layer OWNER TO geocab;

--
-- Name: access_group_layer_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE access_group_layer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE geocab.access_group_layer_id_seq OWNER TO geocab;

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


ALTER TABLE geocab.access_group_tool OWNER TO geocab;

--
-- Name: access_group_tool_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE access_group_tool_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE geocab.access_group_tool_id_seq OWNER TO geocab;

--
-- Name: access_group_user; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE access_group_user (
    access_group_id bigint NOT NULL,
    user_username bigint NOT NULL
);


ALTER TABLE geocab.access_group_user OWNER TO geocab;

--
-- Name: access_group_user_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE access_group_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE geocab.access_group_user_id_seq OWNER TO geocab;

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
    order_attribute integer
);


ALTER TABLE geocab.attribute OWNER TO geocab;

--
-- Name: attribute_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE attribute_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE geocab.attribute_id_seq OWNER TO geocab;

--
-- Name: attribute_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE attribute_id_seq OWNED BY attribute.id;


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


ALTER TABLE geocab.custom_search OWNER TO geocab;

--
-- Name: custom_search_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE custom_search_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE geocab.custom_search_id_seq OWNER TO geocab;

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


ALTER TABLE geocab.data_source OWNER TO geocab;

--
-- Name: data_source_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE data_source_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE geocab.data_source_id_seq OWNER TO geocab;

--
-- Name: data_source_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE data_source_id_seq OWNED BY data_source.id;


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


ALTER TABLE geocab.layer OWNER TO geocab;

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


ALTER TABLE geocab.layer_field OWNER TO geocab;

--
-- Name: layer_field_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE layer_field_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE geocab.layer_field_id_seq OWNER TO geocab;

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


ALTER TABLE geocab.layer_group OWNER TO geocab;

--
-- Name: layer_group_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE layer_group_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE geocab.layer_group_id_seq OWNER TO geocab;

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


ALTER TABLE geocab.layer_id_seq OWNER TO geocab;

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
    location public.geometry
);


ALTER TABLE geocab.marker OWNER TO geocab;

--
-- Name: marker_attribute; Type: TABLE; Schema: geocab; Owner: geocab; Tablespace: geocab_dados
--

CREATE TABLE marker_attribute (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    updated timestamp without time zone,
    value character varying(255) NOT NULL,
    attribute_id bigint,
    marker_id bigint
);


ALTER TABLE geocab.marker_attribute OWNER TO geocab;

--
-- Name: marker_attribute_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE marker_attribute_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE geocab.marker_attribute_id_seq OWNER TO geocab;

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


ALTER TABLE geocab.marker_id_seq OWNER TO geocab;

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


ALTER TABLE geocab.marker_moderation OWNER TO geocab;

--
-- Name: marker_moderation_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE marker_moderation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE geocab.marker_moderation_id_seq OWNER TO geocab;

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


ALTER TABLE geocab.motive OWNER TO geocab;

--
-- Name: motive_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE motive_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE geocab.motive_id_seq OWNER TO geocab;

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


ALTER TABLE geocab.motive_marker_moderation OWNER TO geocab;

--
-- Name: motive_marker_moderation_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE motive_marker_moderation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE geocab.motive_marker_moderation_id_seq OWNER TO geocab;

--
-- Name: motive_marker_moderation_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE motive_marker_moderation_id_seq OWNED BY motive_marker_moderation.id;


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


ALTER TABLE geocab.tool OWNER TO geocab;

--
-- Name: tool_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE tool_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE geocab.tool_id_seq OWNER TO geocab;

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
    role integer NOT NULL
);


ALTER TABLE geocab."user" OWNER TO geocab;

--
-- Name: user_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE geocab.user_id_seq OWNER TO geocab;

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


ALTER TABLE geocab.user_social_connection OWNER TO geocab;

--
-- Name: user_social_connection_id_seq; Type: SEQUENCE; Schema: geocab; Owner: geocab
--

CREATE SEQUENCE user_social_connection_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE geocab.user_social_connection_id_seq OWNER TO geocab;

--
-- Name: user_social_connection_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab; Owner: geocab
--

ALTER SEQUENCE user_social_connection_id_seq OWNED BY user_social_connection.id;


SET search_path = geocab_auditoria, pg_catalog;

SET default_tablespace = geocab_auditoria_dados;

--
-- Name: access_group_audited; Type: TABLE; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_dados
--

CREATE TABLE access_group_audited (
    id bigint NOT NULL,
    revision bigint NOT NULL,
    revision_type smallint,
    description character varying(255),
    name character varying(144)
);


ALTER TABLE geocab_auditoria.access_group_audited OWNER TO geocab_auditoria;

--
-- Name: access_group_custom_search_audited; Type: TABLE; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_dados
--

CREATE TABLE access_group_custom_search_audited (
    id bigint NOT NULL,
    revision bigint NOT NULL,
    revision_type smallint,
    access_group_id bigint,
    custom_search_id bigint
);


ALTER TABLE geocab_auditoria.access_group_custom_search_audited OWNER TO geocab_auditoria;

--
-- Name: access_group_layer_audited; Type: TABLE; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_dados
--

CREATE TABLE access_group_layer_audited (
    id bigint NOT NULL,
    revision bigint NOT NULL,
    revision_type smallint,
    access_group_id bigint,
    layer_id bigint
);


ALTER TABLE geocab_auditoria.access_group_layer_audited OWNER TO geocab_auditoria;

--
-- Name: access_group_tool_audited; Type: TABLE; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_dados
--

CREATE TABLE access_group_tool_audited (
    revision bigint NOT NULL,
    access_group_id bigint NOT NULL,
    tool_id bigint NOT NULL,
    revision_type smallint
);


ALTER TABLE geocab_auditoria.access_group_tool_audited OWNER TO geocab_auditoria;

--
-- Name: access_group_user_audited; Type: TABLE; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_dados
--

CREATE TABLE access_group_user_audited (
    revision bigint NOT NULL,
    access_group_id bigint NOT NULL,
    user_username bigint NOT NULL,
    revision_type smallint
);


ALTER TABLE geocab_auditoria.access_group_user_audited OWNER TO geocab_auditoria;

--
-- Name: attribute_audited; Type: TABLE; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_dados
--

CREATE TABLE attribute_audited (
    id bigint NOT NULL,
    revision bigint NOT NULL,
    revision_type smallint,
    attribute_default boolean,
    name character varying(255),
    required boolean,
    type integer,
    layer_id bigint,
    order_attribute integer
);


ALTER TABLE geocab_auditoria.attribute_audited OWNER TO geocab_auditoria;

--
-- Name: custom_search_audited; Type: TABLE; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_dados
--

CREATE TABLE custom_search_audited (
    id bigint NOT NULL,
    revision bigint NOT NULL,
    revision_type smallint,
    name character varying(144),
    layer_id bigint
);


ALTER TABLE geocab_auditoria.custom_search_audited OWNER TO geocab_auditoria;

--
-- Name: custom_search_layer_field_audited; Type: TABLE; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_dados
--

CREATE TABLE custom_search_layer_field_audited (
    revision bigint NOT NULL,
    pesquisa_id bigint NOT NULL,
    id bigint NOT NULL,
    revision_type smallint
);


ALTER TABLE geocab_auditoria.custom_search_layer_field_audited OWNER TO geocab_auditoria;

--
-- Name: data_source_audited; Type: TABLE; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_dados
--

CREATE TABLE data_source_audited (
    id bigint NOT NULL,
    revision bigint NOT NULL,
    revision_type smallint,
    login character varying(144),
    name character varying(144),
    password character varying(144),
    url character varying(255)
);


ALTER TABLE geocab_auditoria.data_source_audited OWNER TO geocab_auditoria;

--
-- Name: field_layer_audited; Type: TABLE; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_dados
--

CREATE TABLE field_layer_audited (
    id bigint NOT NULL,
    revision bigint NOT NULL,
    revision_type smallint,
    label character varying(144),
    nome character varying(144),
    order_campo_camada integer,
    tipo integer
);


ALTER TABLE geocab_auditoria.field_layer_audited OWNER TO geocab_auditoria;

--
-- Name: layer_audited; Type: TABLE; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_dados
--

CREATE TABLE layer_audited (
    id bigint NOT NULL,
    revision bigint NOT NULL,
    revision_type smallint,
    enabled boolean,
    icon character varying(255),
    maximum_scale_map integer,
    minimum_scale_map integer,
    name character varying(144),
    order_layer integer,
    published boolean,
    start_enabled boolean,
    start_visible boolean,
    title character varying(144),
    data_source_id bigint,
    layer_group_id bigint,
    published_layer_id bigint
);


ALTER TABLE geocab_auditoria.layer_audited OWNER TO geocab_auditoria;

--
-- Name: layer_field_audited; Type: TABLE; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_dados
--

CREATE TABLE layer_field_audited (
    id bigint NOT NULL,
    revision bigint NOT NULL,
    revision_type smallint,
    label character varying(144),
    name character varying(144),
    order_layer integer,
    type integer,
    attribute_id bigint
);


ALTER TABLE geocab_auditoria.layer_field_audited OWNER TO geocab_auditoria;

--
-- Name: layer_group_audited; Type: TABLE; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_dados
--

CREATE TABLE layer_group_audited (
    id bigint NOT NULL,
    revision bigint NOT NULL,
    revision_type smallint,
    name character varying(144),
    order_layer_group integer,
    published boolean,
    draft_id bigint,
    layer_group_upper_id bigint
);


ALTER TABLE geocab_auditoria.layer_group_audited OWNER TO geocab_auditoria;

--
-- Name: layer_group_layer_audited; Type: TABLE; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_dados
--

CREATE TABLE layer_group_layer_audited (
    revision bigint NOT NULL,
    layer_group_id bigint NOT NULL,
    id bigint NOT NULL,
    revision_type smallint
);


ALTER TABLE geocab_auditoria.layer_group_layer_audited OWNER TO geocab_auditoria;

--
-- Name: layer_group_layer_group_audited; Type: TABLE; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_dados
--

CREATE TABLE layer_group_layer_group_audited (
    revision bigint NOT NULL,
    layer_group_upper_id bigint NOT NULL,
    id bigint NOT NULL,
    revision_type smallint
);


ALTER TABLE geocab_auditoria.layer_group_layer_group_audited OWNER TO geocab_auditoria;

--
-- Name: marker_attribute_audited; Type: TABLE; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_dados
--

CREATE TABLE marker_attribute_audited (
    id bigint NOT NULL,
    revision bigint NOT NULL,
    revision_type smallint,
    value character varying(255),
    attribute_id bigint,
    marker_id bigint
);


ALTER TABLE geocab_auditoria.marker_attribute_audited OWNER TO geocab_auditoria;

--
-- Name: marker_audited; Type: TABLE; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_dados
--

CREATE TABLE marker_audited (
    id bigint NOT NULL,
    revision bigint NOT NULL,
    revision_type smallint,
    deleted boolean,
    status integer,
    layer_id bigint,
    user_id bigint,
    location public.geometry
);


ALTER TABLE geocab_auditoria.marker_audited OWNER TO geocab_auditoria;

--
-- Name: marker_moderation_audited; Type: TABLE; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_dados
--

CREATE TABLE marker_moderation_audited (
    id bigint NOT NULL,
    revision bigint NOT NULL,
    revision_type smallint,
    status_description character varying(144),
    marker_id bigint,
    status integer
);


ALTER TABLE geocab_auditoria.marker_moderation_audited OWNER TO geocab_auditoria;

--
-- Name: motive_audited; Type: TABLE; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_dados
--

CREATE TABLE motive_audited (
    id bigint NOT NULL,
    revision bigint NOT NULL,
    revision_type smallint,
    name character varying(255)
);


ALTER TABLE geocab_auditoria.motive_audited OWNER TO geocab_auditoria;

--
-- Name: motive_marker_moderation_audited; Type: TABLE; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_dados
--

CREATE TABLE motive_marker_moderation_audited (
    id bigint NOT NULL,
    revision bigint NOT NULL,
    revision_type smallint,
    value character varying(255),
    marker_moderation_id bigint,
    motive_id bigint,
    description character varying(255)
);


ALTER TABLE geocab_auditoria.motive_marker_moderation_audited OWNER TO geocab_auditoria;

--
-- Name: revision; Type: TABLE; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_dados
--

CREATE TABLE revision (
    id bigint NOT NULL,
    "timestamp" bigint NOT NULL,
    user_id bigint
);


ALTER TABLE geocab_auditoria.revision OWNER TO geocab_auditoria;

--
-- Name: revision_id_seq; Type: SEQUENCE; Schema: geocab_auditoria; Owner: geocab_auditoria
--

CREATE SEQUENCE revision_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE geocab_auditoria.revision_id_seq OWNER TO geocab_auditoria;

--
-- Name: revision_id_seq; Type: SEQUENCE OWNED BY; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER SEQUENCE revision_id_seq OWNED BY revision.id;


--
-- Name: tool_audited; Type: TABLE; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_dados
--

CREATE TABLE tool_audited (
    id bigint NOT NULL,
    revision bigint NOT NULL,
    revision_type smallint,
    description character varying(255),
    name character varying(144)
);


ALTER TABLE geocab_auditoria.tool_audited OWNER TO geocab_auditoria;

--
-- Name: user_audited; Type: TABLE; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_dados
--

CREATE TABLE user_audited (
    id bigint NOT NULL,
    revision bigint NOT NULL,
    revision_type smallint,
    email character varying(144),
    enabled boolean,
    name character varying(50),
    password character varying(100),
    role integer
);


ALTER TABLE geocab_auditoria.user_audited OWNER TO geocab_auditoria;

SET search_path = geocab, pg_catalog;

--
-- Name: id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY access_group ALTER COLUMN id SET DEFAULT nextval('access_group_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY access_group_custom_search ALTER COLUMN id SET DEFAULT nextval('access_group_custom_search_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY access_group_layer ALTER COLUMN id SET DEFAULT nextval('access_group_layer_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY attribute ALTER COLUMN id SET DEFAULT nextval('attribute_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY custom_search ALTER COLUMN id SET DEFAULT nextval('custom_search_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY data_source ALTER COLUMN id SET DEFAULT nextval('data_source_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY layer ALTER COLUMN id SET DEFAULT nextval('layer_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY layer_field ALTER COLUMN id SET DEFAULT nextval('layer_field_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY layer_group ALTER COLUMN id SET DEFAULT nextval('layer_group_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY marker ALTER COLUMN id SET DEFAULT nextval('marker_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY marker_attribute ALTER COLUMN id SET DEFAULT nextval('marker_attribute_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY marker_moderation ALTER COLUMN id SET DEFAULT nextval('marker_moderation_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY motive ALTER COLUMN id SET DEFAULT nextval('motive_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY motive_marker_moderation ALTER COLUMN id SET DEFAULT nextval('motive_marker_moderation_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY tool ALTER COLUMN id SET DEFAULT nextval('tool_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY "user" ALTER COLUMN id SET DEFAULT nextval('user_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY user_social_connection ALTER COLUMN id SET DEFAULT nextval('user_social_connection_id_seq'::regclass);


SET search_path = geocab_auditoria, pg_catalog;

--
-- Name: id; Type: DEFAULT; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER TABLE ONLY revision ALTER COLUMN id SET DEFAULT nextval('revision_id_seq'::regclass);


SET search_path = geocab, pg_catalog;

--
-- Data for Name: access_group; Type: TABLE DATA; Schema: geocab; Owner: geocab
--

COPY access_group (id, created, updated, description, name) FROM stdin;
1	2014-12-09 17:05:57.714	\N	Este é um grupo de acesso público 	Grupo de acesso público
\.


--
-- Data for Name: access_group_custom_search; Type: TABLE DATA; Schema: geocab; Owner: geocab
--

COPY access_group_custom_search (id, created, updated, access_group_id, custom_search_id) FROM stdin;
\.


--
-- Name: access_group_custom_search_id_seq; Type: SEQUENCE SET; Schema: geocab; Owner: geocab
--

SELECT pg_catalog.setval('access_group_custom_search_id_seq', 1, false);


--
-- Name: access_group_id_seq; Type: SEQUENCE SET; Schema: geocab; Owner: geocab
--

SELECT pg_catalog.setval('access_group_id_seq', 1, false);


--
-- Data for Name: access_group_layer; Type: TABLE DATA; Schema: geocab; Owner: geocab
--

COPY access_group_layer (id, created, updated, access_group_id, layer_id) FROM stdin;
\.


--
-- Name: access_group_layer_id_seq; Type: SEQUENCE SET; Schema: geocab; Owner: geocab
--

SELECT pg_catalog.setval('access_group_layer_id_seq', 1, false);


--
-- Data for Name: access_group_tool; Type: TABLE DATA; Schema: geocab; Owner: geocab
--

COPY access_group_tool (access_group_id, tool_id) FROM stdin;
\.


--
-- Name: access_group_tool_id_seq; Type: SEQUENCE SET; Schema: geocab; Owner: geocab
--

SELECT pg_catalog.setval('access_group_tool_id_seq', 1, false);


--
-- Data for Name: access_group_user; Type: TABLE DATA; Schema: geocab; Owner: geocab
--

COPY access_group_user (access_group_id, user_username) FROM stdin;
\.


--
-- Name: access_group_user_id_seq; Type: SEQUENCE SET; Schema: geocab; Owner: geocab
--

SELECT pg_catalog.setval('access_group_user_id_seq', 1, false);


--
-- Data for Name: attribute; Type: TABLE DATA; Schema: geocab; Owner: geocab
--

COPY attribute (id, created, updated, attribute_default, name, required, type, layer_id, order_attribute) FROM stdin;
1	2014-11-20 16:40:39.587	\N	\N	Capacidade (m3)	t	1	1	0
2	2014-11-20 16:40:39.606	\N	\N	Descrição	f	0	1	1
3	2014-11-26 17:40:40.3	\N	\N	Descrição	t	0	5	2
4	2014-11-26 17:40:40.309	\N	\N	Data Ocupação	t	2	5	3
5	2014-11-26 17:40:40.311	\N	\N	Própria	t	3	5	4
\.


--
-- Name: attribute_id_seq; Type: SEQUENCE SET; Schema: geocab; Owner: geocab
--

SELECT pg_catalog.setval('attribute_id_seq', 5, true);


--
-- Data for Name: custom_search; Type: TABLE DATA; Schema: geocab; Owner: geocab
--

COPY custom_search (id, created, updated, name, layer_id) FROM stdin;
\.


--
-- Name: custom_search_id_seq; Type: SEQUENCE SET; Schema: geocab; Owner: geocab
--

SELECT pg_catalog.setval('custom_search_id_seq', 1, false);


--
-- Data for Name: data_source; Type: TABLE DATA; Schema: geocab; Owner: geocab
--

COPY data_source (id, created, updated, login, name, password, url) FROM stdin;
4	2014-11-20 16:37:38.638	\N	\N	Itaipu	\N	\N
5	2014-11-20 16:46:38.07	\N	\N	Geoserver Itaipu	\N	http://geoserver.itaipu/geoserver/ows?service=wms&version=1.3.0&request=GetCapabilities
\.


--
-- Name: data_source_id_seq; Type: SEQUENCE SET; Schema: geocab; Owner: geocab
--

SELECT pg_catalog.setval('data_source_id_seq', 5, true);


--
-- Data for Name: layer; Type: TABLE DATA; Schema: geocab; Owner: geocab
--

COPY layer (id, created, updated, enabled, icon, maximum_scale_map, minimum_scale_map, name, order_layer, published, start_enabled, start_visible, title, data_source_id, layer_group_id, published_layer_id) FROM stdin;
1	2014-11-20 16:40:39.578	2014-11-20 16:41:23.948	t	static/icons/watertower.png	13	0	Cisternas	0	f	t	t	Cisternas	4	1	2
2	2014-11-20 16:41:23.944	\N	\N	\N	13	0	Cisternas	0	t	f	f	Cisternas	\N	2	\N
3	2014-11-20 16:47:19.813	2014-11-26 17:40:46.923	f	static/icons/default_blue.png	13	0	bdgeo:v_microbacia_hidrografica_regional	2	f	t	t	P240 - Microbracia Hidrografica Regional	5	1	4
4	2014-11-20 16:47:32.289	2014-11-26 17:43:38.466	\N	\N	13	0	bdgeo:v_microbacia_hidrografica_regional	2	t	f	f	P240 - Microbracia Hidrografica Regional	\N	2	\N
6	2014-11-26 17:40:48.552	2014-11-26 17:43:38.466	\N	\N	13	9	Casa dos Funcionários	1	t	f	f	Casa dos Funcionários	\N	2	\N
5	2014-11-26 17:40:40.219	2014-11-26 17:45:09.567	t	static/icons/jewishquarter.png	13	0	Casa dos Funcionários	1	f	t	t	Casa dos Funcionários	4	1	6
\.


--
-- Data for Name: layer_field; Type: TABLE DATA; Schema: geocab; Owner: geocab
--

COPY layer_field (id, created, updated, label, name, order_layer, type, pesquisa_id, attribute_id) FROM stdin;
\.


--
-- Name: layer_field_id_seq; Type: SEQUENCE SET; Schema: geocab; Owner: geocab
--

SELECT pg_catalog.setval('layer_field_id_seq', 1, false);


--
-- Data for Name: layer_group; Type: TABLE DATA; Schema: geocab; Owner: geocab
--

COPY layer_group (id, created, updated, name, order_layer_group, published, draft_id, layer_group_upper_id) FROM stdin;
1	2014-11-20 16:40:15.897	\N	Abastecimento	0	f	\N	\N
2	2014-11-20 16:40:15.897	\N	Abastecimento	0	t	1	\N
\.


--
-- Name: layer_group_id_seq; Type: SEQUENCE SET; Schema: geocab; Owner: geocab
--

SELECT pg_catalog.setval('layer_group_id_seq', 2, true);


--
-- Name: layer_id_seq; Type: SEQUENCE SET; Schema: geocab; Owner: geocab
--

SELECT pg_catalog.setval('layer_id_seq', 6, true);


--
-- Data for Name: marker; Type: TABLE DATA; Schema: geocab; Owner: geocab
--

COPY marker (id, created, updated, deleted, status, layer_id, user_id, location) FROM stdin;
\.


--
-- Data for Name: marker_attribute; Type: TABLE DATA; Schema: geocab; Owner: geocab
--

COPY marker_attribute (id, created, updated, value, attribute_id, marker_id) FROM stdin;
\.


--
-- Name: marker_attribute_id_seq; Type: SEQUENCE SET; Schema: geocab; Owner: geocab
--

SELECT pg_catalog.setval('marker_attribute_id_seq', 10, true);


--
-- Name: marker_id_seq; Type: SEQUENCE SET; Schema: geocab; Owner: geocab
--

SELECT pg_catalog.setval('marker_id_seq', 4, true);


--
-- Data for Name: marker_moderation; Type: TABLE DATA; Schema: geocab; Owner: geocab
--

COPY marker_moderation (id, created, updated, marker_id, status) FROM stdin;
\.


--
-- Name: marker_moderation_id_seq; Type: SEQUENCE SET; Schema: geocab; Owner: geocab
--

SELECT pg_catalog.setval('marker_moderation_id_seq', 1, false);


--
-- Data for Name: motive; Type: TABLE DATA; Schema: geocab; Owner: geocab
--

COPY motive (id, created, updated, name) FROM stdin;
\.


--
-- Name: motive_id_seq; Type: SEQUENCE SET; Schema: geocab; Owner: geocab
--

SELECT pg_catalog.setval('motive_id_seq', 1, false);


--
-- Data for Name: motive_marker_moderation; Type: TABLE DATA; Schema: geocab; Owner: geocab
--

COPY motive_marker_moderation (id, created, updated, marker_moderation_id, motive_id, description) FROM stdin;
\.


--
-- Name: motive_marker_moderation_id_seq; Type: SEQUENCE SET; Schema: geocab; Owner: geocab
--

SELECT pg_catalog.setval('motive_marker_moderation_id_seq', 1, false);


--
-- Data for Name: tool; Type: TABLE DATA; Schema: geocab; Owner: geocab
--

COPY tool (id, created, updated, description, name) FROM stdin;
1	2014-09-17 09:30:19.144748	\N	Ferramenta para medir distancia	Medir distancia
2	2014-09-17 09:30:19.144748	\N	Ferramenta para medir area	Medir area
3	2014-09-17 09:30:19.144748	\N	Ferramenta para KML	Habilitar KML
\.


--
-- Name: tool_id_seq; Type: SEQUENCE SET; Schema: geocab; Owner: geocab
--

SELECT pg_catalog.setval('tool_id_seq', 1, false);


--
-- Data for Name: user; Type: TABLE DATA; Schema: geocab; Owner: geocab
--

COPY "user" (id, created, updated, email, enabled, name, password, role) FROM stdin;
2	2014-11-03 09:31:52.135	2014-11-20 11:05:42.385	admin@admin.com	f	admin	92148f305c6b896aced78fd57d7526c8c38fb08c	0
100	2014-11-20 09:58:27.176607	2014-12-03 14:03:36.681	geocab@itaipu.gov.br	t	GeoCAB Admin	92148f305c6b896aced78fd57d7526c8c38fb08c	0
3	2014-12-03 18:15:18.535	\N	olimpio.santos@gmail.com	t	Olimpio	bd0626dcd201db4cb0aa07fa5495f93c57994497	2
\.


--
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: geocab; Owner: geocab
--

SELECT pg_catalog.setval('user_id_seq', 3, true);


--
-- Data for Name: user_social_connection; Type: TABLE DATA; Schema: geocab; Owner: geocab
--

COPY user_social_connection (id, access_token, display_name, expire_time, image_url, profile_url, provider_id, provider_user_id, rank, refresh_token, secret, user_id) FROM stdin;
\.


--
-- Name: user_social_connection_id_seq; Type: SEQUENCE SET; Schema: geocab; Owner: geocab
--

SELECT pg_catalog.setval('user_social_connection_id_seq', 1, false);


SET search_path = geocab_auditoria, pg_catalog;

--
-- Data for Name: access_group_audited; Type: TABLE DATA; Schema: geocab_auditoria; Owner: geocab_auditoria
--

COPY access_group_audited (id, revision, revision_type, description, name) FROM stdin;
\.


--
-- Data for Name: access_group_custom_search_audited; Type: TABLE DATA; Schema: geocab_auditoria; Owner: geocab_auditoria
--

COPY access_group_custom_search_audited (id, revision, revision_type, access_group_id, custom_search_id) FROM stdin;
\.


--
-- Data for Name: access_group_layer_audited; Type: TABLE DATA; Schema: geocab_auditoria; Owner: geocab_auditoria
--

COPY access_group_layer_audited (id, revision, revision_type, access_group_id, layer_id) FROM stdin;
\.


--
-- Data for Name: access_group_tool_audited; Type: TABLE DATA; Schema: geocab_auditoria; Owner: geocab_auditoria
--

COPY access_group_tool_audited (revision, access_group_id, tool_id, revision_type) FROM stdin;
\.


--
-- Data for Name: access_group_user_audited; Type: TABLE DATA; Schema: geocab_auditoria; Owner: geocab_auditoria
--

COPY access_group_user_audited (revision, access_group_id, user_username, revision_type) FROM stdin;
\.


--
-- Data for Name: attribute_audited; Type: TABLE DATA; Schema: geocab_auditoria; Owner: geocab_auditoria
--

COPY attribute_audited (id, revision, revision_type, attribute_default, name, required, type, layer_id, order_attribute) FROM stdin;
1	7	0	\N	Capacidade (m3)	t	1	1	\N
2	7	0	\N	Descrição	f	0	1	\N
1	9	1	\N	\N	\N	\N	\N	\N
2	9	1	\N	\N	\N	\N	\N	\N
1	16	1	\N	\N	\N	\N	\N	\N
2	16	1	\N	\N	\N	\N	\N	\N
3	17	0	\N	Descrição	t	0	5	\N
4	17	0	\N	Data Ocupação	t	2	5	\N
5	17	0	\N	Própria	t	3	5	\N
3	20	1	\N	\N	\N	\N	\N	\N
4	20	1	\N	\N	\N	\N	\N	\N
5	20	1	\N	\N	\N	\N	\N	\N
3	30	1	\N	\N	\N	\N	\N	\N
4	30	1	\N	\N	\N	\N	\N	\N
5	30	1	\N	\N	\N	\N	\N	\N
\.


--
-- Data for Name: custom_search_audited; Type: TABLE DATA; Schema: geocab_auditoria; Owner: geocab_auditoria
--

COPY custom_search_audited (id, revision, revision_type, name, layer_id) FROM stdin;
\.


--
-- Data for Name: custom_search_layer_field_audited; Type: TABLE DATA; Schema: geocab_auditoria; Owner: geocab_auditoria
--

COPY custom_search_layer_field_audited (revision, pesquisa_id, id, revision_type) FROM stdin;
\.


--
-- Data for Name: data_source_audited; Type: TABLE DATA; Schema: geocab_auditoria; Owner: geocab_auditoria
--

COPY data_source_audited (id, revision, revision_type, login, name, password, url) FROM stdin;
3	1	0	\N	sadsad	\N	\N
3	2	2	\N	\N	\N	\N
4	5	0	\N	Itaipu	\N	\N
5	11	0	\N	Geoserver Itaipu	\N	http://geoserver.itaipu/geoserver/ows?service=wms&version=1.3.0&request=GetCapabilities
\.


--
-- Data for Name: field_layer_audited; Type: TABLE DATA; Schema: geocab_auditoria; Owner: geocab_auditoria
--

COPY field_layer_audited (id, revision, revision_type, label, nome, order_campo_camada, tipo) FROM stdin;
\.


--
-- Data for Name: layer_audited; Type: TABLE DATA; Schema: geocab_auditoria; Owner: geocab_auditoria
--

COPY layer_audited (id, revision, revision_type, enabled, icon, maximum_scale_map, minimum_scale_map, name, order_layer, published, start_enabled, start_visible, title, data_source_id, layer_group_id, published_layer_id) FROM stdin;
1	7	0	t	static/icons/watertower.png	15	0	Cisternas	0	f	t	t	Cisternas	4	1	\N
2	8	0	\N	\N	15	0	Cisternas	0	t	f	f	Cisternas	\N	2	\N
1	8	1	t	static/icons/watertower.png	15	0	Cisternas	0	f	t	t	Cisternas	4	1	2
1	9	1	\N	\N	\N	\N	\N	0	\N	f	f	\N	\N	\N	\N
3	12	0	f	static/icons/default_blue.png	15	0	bdgeo:v_microbacia_hidrografica_regional	0	f	t	t	P240 - Microbracia Hidrografica Regional	5	1	\N
3	13	1	f	static/icons/default_blue.png	15	0	bdgeo:v_microbacia_hidrografica_regional	1	f	t	t	P240 - Microbracia Hidrografica Regional	5	1	\N
4	14	0	\N	\N	15	0	bdgeo:v_microbacia_hidrografica_regional	1	t	f	f	P240 - Microbracia Hidrografica Regional	\N	2	\N
3	14	1	f	static/icons/default_blue.png	15	0	bdgeo:v_microbacia_hidrografica_regional	1	f	t	t	P240 - Microbracia Hidrografica Regional	5	1	4
1	16	1	\N	\N	\N	\N	\N	0	\N	f	f	\N	\N	\N	\N
5	17	0	t	static/icons/parkinggarage.png	15	0	Casa dos Funcionários	0	f	t	t	Casa dos Funcionários	4	1	\N
5	18	1	t	static/icons/parkinggarage.png	15	0	Casa dos Funcionários	1	f	t	t	Casa dos Funcionários	4	1	\N
3	18	1	f	static/icons/default_blue.png	13	0	bdgeo:v_microbacia_hidrografica_regional	2	f	t	t	P240 - Microbracia Hidrografica Regional	5	1	4
6	19	0	\N	\N	15	0	Casa dos Funcionários	1	t	f	f	Casa dos Funcionários	\N	2	\N
5	19	1	t	static/icons/parkinggarage.png	15	0	Casa dos Funcionários	1	f	t	t	Casa dos Funcionários	4	1	6
4	19	1	\N	\N	13	0	bdgeo:v_microbacia_hidrografica_regional	2	t	f	f	P240 - Microbracia Hidrografica Regional	\N	2	\N
5	20	1	\N	\N	\N	\N	\N	0	\N	f	f	\N	\N	\N	\N
5	21	1	t	static/icons/parkinggarage.png	15	9	Casa dos Funcionários	1	f	t	t	Casa dos Funcionários	4	1	6
6	22	1	\N	\N	15	9	Casa dos Funcionários	1	t	f	f	Casa dos Funcionários	\N	2	\N
5	23	1	t	static/icons/parkinggarage.png	2	0	Casa dos Funcionários	1	f	t	t	Casa dos Funcionários	4	1	6
5	24	1	t	static/icons/parkinggarage.png	15	0	Casa dos Funcionários	1	f	t	t	Casa dos Funcionários	4	1	6
5	25	1	t	static/icons/jewishquarter.png	15	0	Casa dos Funcionários	1	f	t	t	Casa dos Funcionários	4	1	6
5	30	1	\N	\N	\N	\N	\N	0	\N	f	f	\N	\N	\N	\N
\.


--
-- Data for Name: layer_field_audited; Type: TABLE DATA; Schema: geocab_auditoria; Owner: geocab_auditoria
--

COPY layer_field_audited (id, revision, revision_type, label, name, order_layer, type, attribute_id) FROM stdin;
\.


--
-- Data for Name: layer_group_audited; Type: TABLE DATA; Schema: geocab_auditoria; Owner: geocab_auditoria
--

COPY layer_group_audited (id, revision, revision_type, name, order_layer_group, published, draft_id, layer_group_upper_id) FROM stdin;
1	6	0	Abastecimento	0	f	\N	\N
2	8	0	Abastecimento	0	t	1	\N
2	14	1	Abastecimento	0	t	1	\N
2	19	1	Abastecimento	0	t	1	\N
\.


--
-- Data for Name: layer_group_layer_audited; Type: TABLE DATA; Schema: geocab_auditoria; Owner: geocab_auditoria
--

COPY layer_group_layer_audited (revision, layer_group_id, id, revision_type) FROM stdin;
8	2	2	0
14	2	4	0
19	2	6	0
\.


--
-- Data for Name: layer_group_layer_group_audited; Type: TABLE DATA; Schema: geocab_auditoria; Owner: geocab_auditoria
--

COPY layer_group_layer_group_audited (revision, layer_group_upper_id, id, revision_type) FROM stdin;
\.


--
-- Data for Name: marker_attribute_audited; Type: TABLE DATA; Schema: geocab_auditoria; Owner: geocab_auditoria
--

COPY marker_attribute_audited (id, revision, revision_type, value, attribute_id, marker_id) FROM stdin;
1	9	0	16000	1	1
2	9	0	teste de cadastro	2	1
3	16	0	15000	1	2
4	16	0	Testando a inserção de dados de uma camada do tipo interna	2	2
5	20	0	Casa do Dirceu	3	3
6	20	0	12/05/2014	4	3
7	20	0	Yes	5	3
8	30	0	testando correção da issue #25	3	4
9	30	0	02/12/2014	4	4
10	30	0	Yes	5	4
\.


--
-- Data for Name: marker_audited; Type: TABLE DATA; Schema: geocab_auditoria; Owner: geocab_auditoria
--

COPY marker_audited (id, revision, revision_type, deleted, status, layer_id, user_id, location) FROM stdin;
1	9	0	\N	2	1	100	\N
1	10	1	\N	0	1	100	\N
1	15	1	t	0	1	100	\N
2	16	0	\N	2	1	100	\N
3	20	0	\N	2	5	100	\N
3	26	1	\N	0	5	100	\N
4	30	0	\N	2	5	3	\N
\.


--
-- Data for Name: marker_moderation_audited; Type: TABLE DATA; Schema: geocab_auditoria; Owner: geocab_auditoria
--

COPY marker_moderation_audited (id, revision, revision_type, status_description, marker_id, status) FROM stdin;
\.


--
-- Data for Name: motive_audited; Type: TABLE DATA; Schema: geocab_auditoria; Owner: geocab_auditoria
--

COPY motive_audited (id, revision, revision_type, name) FROM stdin;
\.


--
-- Data for Name: motive_marker_moderation_audited; Type: TABLE DATA; Schema: geocab_auditoria; Owner: geocab_auditoria
--

COPY motive_marker_moderation_audited (id, revision, revision_type, value, marker_moderation_id, motive_id, description) FROM stdin;
\.


--
-- Data for Name: revision; Type: TABLE DATA; Schema: geocab_auditoria; Owner: geocab_auditoria
--

COPY revision (id, "timestamp", user_id) FROM stdin;
1	1416328507215	2
2	1416488642937	100
3	1416488742429	100
4	1416488759434	100
5	1416508658662	100
6	1416508815955	100
7	1416508839612	100
8	1416508883963	100
9	1416509118196	100
10	1416509130398	100
11	1416509198073	100
12	1416509239818	100
13	1416509249987	100
14	1416509252310	100
15	1416858694629	100
16	1416858826652	100
17	1417030840322	100
18	1417030846933	100
19	1417030848584	100
20	1417030984466	100
21	1417031014176	100
22	1417031018479	100
23	1417031056171	100
24	1417031088555	100
25	1417031109570	100
26	1417031119649	100
27	1417622532157	\N
28	1417622616683	100
29	1417637718542	\N
30	1417703046337	3
\.


--
-- Name: revision_id_seq; Type: SEQUENCE SET; Schema: geocab_auditoria; Owner: geocab_auditoria
--

SELECT pg_catalog.setval('revision_id_seq', 30, true);


--
-- Data for Name: tool_audited; Type: TABLE DATA; Schema: geocab_auditoria; Owner: geocab_auditoria
--

COPY tool_audited (id, revision, revision_type, description, name) FROM stdin;
\.


--
-- Data for Name: user_audited; Type: TABLE DATA; Schema: geocab_auditoria; Owner: geocab_auditoria
--

COPY user_audited (id, revision, revision_type, email, enabled, name, password, role) FROM stdin;
2	3	1	admin@admin.com	f	admin	92148f305c6b896aced78fd57d7526c8c38fb08c	0
100	4	1	geocab@itaipu.gov.br	t	GeoCAB Admin	92148f305c6b896aced78fd57d7526c8c38fb08c	0
100	27	1	geocab@itaipu.gov.br	t	GeoCAB Admin	3545c6ce22afe138c70c942d46583e79d1b1772e	0
100	28	1	geocab@itaipu.gov.br	t	GeoCAB Admin	92148f305c6b896aced78fd57d7526c8c38fb08c	0
3	29	0	olimpio.santos@gmail.com	t	Olimpio	bd0626dcd201db4cb0aa07fa5495f93c57994497	2
\.


SET search_path = public, pg_catalog;

--
-- Data for Name: spatial_ref_sys; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY spatial_ref_sys (srid, auth_name, auth_srid, srtext, proj4text) FROM stdin;
\.


SET search_path = geocab, pg_catalog;

SET default_tablespace = geocab_indices;

--
-- Name: access_group_custom_search_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY access_group_custom_search
    ADD CONSTRAINT access_group_custom_search_pkey PRIMARY KEY (id);


--
-- Name: access_group_layer_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY access_group_layer
    ADD CONSTRAINT access_group_layer_pkey PRIMARY KEY (id);


--
-- Name: access_group_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY access_group
    ADD CONSTRAINT access_group_pkey PRIMARY KEY (id);


--
-- Name: access_group_tool_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY access_group_tool
    ADD CONSTRAINT access_group_tool_pkey PRIMARY KEY (access_group_id, tool_id);


--
-- Name: access_group_user_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY access_group_user
    ADD CONSTRAINT access_group_user_pkey PRIMARY KEY (access_group_id, user_username);


--
-- Name: attribute_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY attribute
    ADD CONSTRAINT attribute_pkey PRIMARY KEY (id);


--
-- Name: custom_search_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY custom_search
    ADD CONSTRAINT custom_search_pkey PRIMARY KEY (id);


--
-- Name: data_source_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY data_source
    ADD CONSTRAINT data_source_pkey PRIMARY KEY (id);


--
-- Name: layer_field_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY layer_field
    ADD CONSTRAINT layer_field_pkey PRIMARY KEY (id);


--
-- Name: layer_group_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY layer_group
    ADD CONSTRAINT layer_group_pkey PRIMARY KEY (id);


--
-- Name: layer_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY layer
    ADD CONSTRAINT layer_pkey PRIMARY KEY (id);


--
-- Name: marker_attribute_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY marker_attribute
    ADD CONSTRAINT marker_attribute_pkey PRIMARY KEY (id);


--
-- Name: marker_moderation_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY marker_moderation
    ADD CONSTRAINT marker_moderation_pkey PRIMARY KEY (id);


--
-- Name: marker_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY marker
    ADD CONSTRAINT marker_pkey PRIMARY KEY (id);


--
-- Name: motive_marker_moderation_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY motive_marker_moderation
    ADD CONSTRAINT motive_marker_moderation_pkey PRIMARY KEY (id);


--
-- Name: motive_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY motive
    ADD CONSTRAINT motive_pkey PRIMARY KEY (id);


--
-- Name: tool_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY tool
    ADD CONSTRAINT tool_pkey PRIMARY KEY (id);


--
-- Name: uk_access_group_name; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY access_group
    ADD CONSTRAINT uk_access_group_name UNIQUE (name);


--
-- Name: uk_custom_search_name; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY custom_search
    ADD CONSTRAINT uk_custom_search_name UNIQUE (name);


--
-- Name: uk_data_source_name; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY data_source
    ADD CONSTRAINT uk_data_source_name UNIQUE (name);


--
-- Name: uk_layer_group_layer_group_upper_id_name; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY layer_group
    ADD CONSTRAINT uk_layer_group_layer_group_upper_id_name UNIQUE (name, layer_group_upper_id);


--
-- Name: uk_tool_name; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY tool
    ADD CONSTRAINT uk_tool_name UNIQUE (name);


--
-- Name: uk_user_email; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT uk_user_email UNIQUE (email);


--
-- Name: uk_user_social_connection_provider_id_rank_user_id; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY user_social_connection
    ADD CONSTRAINT uk_user_social_connection_provider_id_rank_user_id UNIQUE (user_id, provider_id, rank);


--
-- Name: user_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: user_social_connection_pkey; Type: CONSTRAINT; Schema: geocab; Owner: geocab; Tablespace: geocab_indices
--

ALTER TABLE ONLY user_social_connection
    ADD CONSTRAINT user_social_connection_pkey PRIMARY KEY (id);


SET search_path = geocab_auditoria, pg_catalog;

SET default_tablespace = geocab_auditoria_indices;

--
-- Name: access_group_audited_pkey; Type: CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_indices
--

ALTER TABLE ONLY access_group_audited
    ADD CONSTRAINT access_group_audited_pkey PRIMARY KEY (id, revision);


--
-- Name: access_group_custom_search_audited_pkey; Type: CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_indices
--

ALTER TABLE ONLY access_group_custom_search_audited
    ADD CONSTRAINT access_group_custom_search_audited_pkey PRIMARY KEY (id, revision);


--
-- Name: access_group_layer_audited_pkey; Type: CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_indices
--

ALTER TABLE ONLY access_group_layer_audited
    ADD CONSTRAINT access_group_layer_audited_pkey PRIMARY KEY (id, revision);


--
-- Name: access_group_tool_audited_pkey; Type: CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_indices
--

ALTER TABLE ONLY access_group_tool_audited
    ADD CONSTRAINT access_group_tool_audited_pkey PRIMARY KEY (revision, access_group_id, tool_id);


--
-- Name: access_group_user_audited_pkey; Type: CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_indices
--

ALTER TABLE ONLY access_group_user_audited
    ADD CONSTRAINT access_group_user_audited_pkey PRIMARY KEY (revision, access_group_id, user_username);


--
-- Name: attribute_audited_pkey; Type: CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_indices
--

ALTER TABLE ONLY attribute_audited
    ADD CONSTRAINT attribute_audited_pkey PRIMARY KEY (id, revision);


--
-- Name: custom_search_audited_pkey; Type: CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_indices
--

ALTER TABLE ONLY custom_search_audited
    ADD CONSTRAINT custom_search_audited_pkey PRIMARY KEY (id, revision);


--
-- Name: custom_search_layer_field_audited_pkey; Type: CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_indices
--

ALTER TABLE ONLY custom_search_layer_field_audited
    ADD CONSTRAINT custom_search_layer_field_audited_pkey PRIMARY KEY (revision, pesquisa_id, id);


--
-- Name: data_source_audited_pkey; Type: CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_indices
--

ALTER TABLE ONLY data_source_audited
    ADD CONSTRAINT data_source_audited_pkey PRIMARY KEY (id, revision);


--
-- Name: field_layer_audited_pkey; Type: CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_indices
--

ALTER TABLE ONLY field_layer_audited
    ADD CONSTRAINT field_layer_audited_pkey PRIMARY KEY (id, revision);


--
-- Name: layer_audited_pkey; Type: CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_indices
--

ALTER TABLE ONLY layer_audited
    ADD CONSTRAINT layer_audited_pkey PRIMARY KEY (id, revision);


--
-- Name: layer_field_audited_pkey; Type: CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_indices
--

ALTER TABLE ONLY layer_field_audited
    ADD CONSTRAINT layer_field_audited_pkey PRIMARY KEY (id, revision);


--
-- Name: layer_group_audited_pkey; Type: CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_indices
--

ALTER TABLE ONLY layer_group_audited
    ADD CONSTRAINT layer_group_audited_pkey PRIMARY KEY (id, revision);


--
-- Name: layer_group_layer_audited_pkey; Type: CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_indices
--

ALTER TABLE ONLY layer_group_layer_audited
    ADD CONSTRAINT layer_group_layer_audited_pkey PRIMARY KEY (revision, layer_group_id, id);


--
-- Name: layer_group_layer_group_audited_pkey; Type: CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_indices
--

ALTER TABLE ONLY layer_group_layer_group_audited
    ADD CONSTRAINT layer_group_layer_group_audited_pkey PRIMARY KEY (revision, layer_group_upper_id, id);


--
-- Name: marker_attribute_audited_pkey; Type: CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_indices
--

ALTER TABLE ONLY marker_attribute_audited
    ADD CONSTRAINT marker_attribute_audited_pkey PRIMARY KEY (id, revision);


--
-- Name: marker_audited_pkey; Type: CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_indices
--

ALTER TABLE ONLY marker_audited
    ADD CONSTRAINT marker_audited_pkey PRIMARY KEY (id, revision);


--
-- Name: marker_moderation_audited_pkey; Type: CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_indices
--

ALTER TABLE ONLY marker_moderation_audited
    ADD CONSTRAINT marker_moderation_audited_pkey PRIMARY KEY (id, revision);


--
-- Name: motive_audited_pkey; Type: CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_indices
--

ALTER TABLE ONLY motive_audited
    ADD CONSTRAINT motive_audited_pkey PRIMARY KEY (id, revision);


--
-- Name: motive_marker_moderation_audited_pkey; Type: CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_indices
--

ALTER TABLE ONLY motive_marker_moderation_audited
    ADD CONSTRAINT motive_marker_moderation_audited_pkey PRIMARY KEY (id, revision);


--
-- Name: revision_pkey; Type: CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_indices
--

ALTER TABLE ONLY revision
    ADD CONSTRAINT revision_pkey PRIMARY KEY (id);


--
-- Name: tool_audited_pkey; Type: CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_indices
--

ALTER TABLE ONLY tool_audited
    ADD CONSTRAINT tool_audited_pkey PRIMARY KEY (id, revision);


--
-- Name: user_audited_pkey; Type: CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria; Tablespace: geocab_auditoria_indices
--

ALTER TABLE ONLY user_audited
    ADD CONSTRAINT user_audited_pkey PRIMARY KEY (id, revision);


SET search_path = geocab, pg_catalog;

--
-- Name: fk_access_group_custom_search_access_group_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY access_group_custom_search
    ADD CONSTRAINT fk_access_group_custom_search_access_group_id FOREIGN KEY (access_group_id) REFERENCES access_group(id);


--
-- Name: fk_access_group_custom_search_custom_search_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY access_group_custom_search
    ADD CONSTRAINT fk_access_group_custom_search_custom_search_id FOREIGN KEY (custom_search_id) REFERENCES custom_search(id);


--
-- Name: fk_access_group_layer_access_group_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY access_group_layer
    ADD CONSTRAINT fk_access_group_layer_access_group_id FOREIGN KEY (access_group_id) REFERENCES access_group(id);


--
-- Name: fk_access_group_layer_layer_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY access_group_layer
    ADD CONSTRAINT fk_access_group_layer_layer_id FOREIGN KEY (layer_id) REFERENCES layer(id);


--
-- Name: fk_access_group_tool_access_group_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY access_group_tool
    ADD CONSTRAINT fk_access_group_tool_access_group_id FOREIGN KEY (access_group_id) REFERENCES access_group(id);


--
-- Name: fk_access_group_tool_tool_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY access_group_tool
    ADD CONSTRAINT fk_access_group_tool_tool_id FOREIGN KEY (tool_id) REFERENCES tool(id);


--
-- Name: fk_access_group_user_access_group_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY access_group_user
    ADD CONSTRAINT fk_access_group_user_access_group_id FOREIGN KEY (access_group_id) REFERENCES access_group(id);


--
-- Name: fk_access_group_user_user_username; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY access_group_user
    ADD CONSTRAINT fk_access_group_user_user_username FOREIGN KEY (user_username) REFERENCES "user"(id);


--
-- Name: fk_attribute_layer_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY attribute
    ADD CONSTRAINT fk_attribute_layer_id FOREIGN KEY (layer_id) REFERENCES layer(id);


--
-- Name: fk_custom_search_layer_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY custom_search
    ADD CONSTRAINT fk_custom_search_layer_id FOREIGN KEY (layer_id) REFERENCES layer(id);


--
-- Name: fk_layer_data_source_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY layer
    ADD CONSTRAINT fk_layer_data_source_id FOREIGN KEY (data_source_id) REFERENCES data_source(id);


--
-- Name: fk_layer_field_pesquisa_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY layer_field
    ADD CONSTRAINT fk_layer_field_pesquisa_id FOREIGN KEY (pesquisa_id) REFERENCES custom_search(id);


--
-- Name: fk_layer_group_draft_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY layer_group
    ADD CONSTRAINT fk_layer_group_draft_id FOREIGN KEY (draft_id) REFERENCES layer_group(id);


--
-- Name: fk_layer_group_layer_group_upper_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY layer_group
    ADD CONSTRAINT fk_layer_group_layer_group_upper_id FOREIGN KEY (layer_group_upper_id) REFERENCES layer_group(id);


--
-- Name: fk_layer_layer_group_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY layer
    ADD CONSTRAINT fk_layer_layer_group_id FOREIGN KEY (layer_group_id) REFERENCES layer_group(id);


--
-- Name: fk_layer_published_layer_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY layer
    ADD CONSTRAINT fk_layer_published_layer_id FOREIGN KEY (published_layer_id) REFERENCES layer(id);


--
-- Name: fk_marker_attribute_attribute_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY marker_attribute
    ADD CONSTRAINT fk_marker_attribute_attribute_id FOREIGN KEY (attribute_id) REFERENCES attribute(id);


--
-- Name: fk_marker_attribute_marker_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY marker_attribute
    ADD CONSTRAINT fk_marker_attribute_marker_id FOREIGN KEY (marker_id) REFERENCES marker(id);


--
-- Name: fk_marker_layer_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY marker
    ADD CONSTRAINT fk_marker_layer_id FOREIGN KEY (layer_id) REFERENCES layer(id);


--
-- Name: fk_marker_moderation_marker_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY marker_moderation
    ADD CONSTRAINT fk_marker_moderation_marker_id FOREIGN KEY (marker_id) REFERENCES marker(id);


--
-- Name: fk_marker_user_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY marker
    ADD CONSTRAINT fk_marker_user_id FOREIGN KEY (user_id) REFERENCES "user"(id);


--
-- Name: fk_motive_marker_moderation_marker_moderation_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY motive_marker_moderation
    ADD CONSTRAINT fk_motive_marker_moderation_marker_moderation_id FOREIGN KEY (marker_moderation_id) REFERENCES marker_moderation(id);


--
-- Name: fk_motive_marker_moderation_motive_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY motive_marker_moderation
    ADD CONSTRAINT fk_motive_marker_moderation_motive_id FOREIGN KEY (motive_id) REFERENCES motive(id);


--
-- Name: fk_user_social_connection_user_id; Type: FK CONSTRAINT; Schema: geocab; Owner: geocab
--

ALTER TABLE ONLY user_social_connection
    ADD CONSTRAINT fk_user_social_connection_user_id FOREIGN KEY (user_id) REFERENCES "user"(id);


SET search_path = geocab_auditoria, pg_catalog;

--
-- Name: fk_access_group_audited_revision; Type: FK CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER TABLE ONLY access_group_audited
    ADD CONSTRAINT fk_access_group_audited_revision FOREIGN KEY (revision) REFERENCES revision(id);


--
-- Name: fk_access_group_custom_search_audited_revision; Type: FK CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER TABLE ONLY access_group_custom_search_audited
    ADD CONSTRAINT fk_access_group_custom_search_audited_revision FOREIGN KEY (revision) REFERENCES revision(id);


--
-- Name: fk_access_group_layer_audited_revision; Type: FK CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER TABLE ONLY access_group_layer_audited
    ADD CONSTRAINT fk_access_group_layer_audited_revision FOREIGN KEY (revision) REFERENCES revision(id);


--
-- Name: fk_access_group_tool_audited_revision; Type: FK CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER TABLE ONLY access_group_tool_audited
    ADD CONSTRAINT fk_access_group_tool_audited_revision FOREIGN KEY (revision) REFERENCES revision(id);


--
-- Name: fk_access_group_user_audited_revision; Type: FK CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER TABLE ONLY access_group_user_audited
    ADD CONSTRAINT fk_access_group_user_audited_revision FOREIGN KEY (revision) REFERENCES revision(id);


--
-- Name: fk_attribute_audited_revision; Type: FK CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER TABLE ONLY attribute_audited
    ADD CONSTRAINT fk_attribute_audited_revision FOREIGN KEY (revision) REFERENCES revision(id);


--
-- Name: fk_custom_search_audited_revision; Type: FK CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER TABLE ONLY custom_search_audited
    ADD CONSTRAINT fk_custom_search_audited_revision FOREIGN KEY (revision) REFERENCES revision(id);


--
-- Name: fk_custom_search_layer_field_audited_revision; Type: FK CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER TABLE ONLY custom_search_layer_field_audited
    ADD CONSTRAINT fk_custom_search_layer_field_audited_revision FOREIGN KEY (revision) REFERENCES revision(id);


--
-- Name: fk_data_source_audited_revision; Type: FK CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER TABLE ONLY data_source_audited
    ADD CONSTRAINT fk_data_source_audited_revision FOREIGN KEY (revision) REFERENCES revision(id);


--
-- Name: fk_field_layer_audited_revision; Type: FK CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER TABLE ONLY field_layer_audited
    ADD CONSTRAINT fk_field_layer_audited_revision FOREIGN KEY (revision) REFERENCES revision(id);


--
-- Name: fk_layer_audited_revision; Type: FK CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER TABLE ONLY layer_audited
    ADD CONSTRAINT fk_layer_audited_revision FOREIGN KEY (revision) REFERENCES revision(id);


--
-- Name: fk_layer_field_audited_revision; Type: FK CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER TABLE ONLY layer_field_audited
    ADD CONSTRAINT fk_layer_field_audited_revision FOREIGN KEY (revision) REFERENCES revision(id);


--
-- Name: fk_layer_group_audited_revision; Type: FK CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER TABLE ONLY layer_group_audited
    ADD CONSTRAINT fk_layer_group_audited_revision FOREIGN KEY (revision) REFERENCES revision(id);


--
-- Name: fk_layer_group_layer_audited_revision; Type: FK CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER TABLE ONLY layer_group_layer_audited
    ADD CONSTRAINT fk_layer_group_layer_audited_revision FOREIGN KEY (revision) REFERENCES revision(id);


--
-- Name: fk_layer_group_layer_group_audited_revision; Type: FK CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER TABLE ONLY layer_group_layer_group_audited
    ADD CONSTRAINT fk_layer_group_layer_group_audited_revision FOREIGN KEY (revision) REFERENCES revision(id);


--
-- Name: fk_marker_attribute_audited_revision; Type: FK CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER TABLE ONLY marker_attribute_audited
    ADD CONSTRAINT fk_marker_attribute_audited_revision FOREIGN KEY (revision) REFERENCES revision(id);


--
-- Name: fk_marker_audited_revision; Type: FK CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER TABLE ONLY marker_audited
    ADD CONSTRAINT fk_marker_audited_revision FOREIGN KEY (revision) REFERENCES revision(id);


--
-- Name: fk_marker_moderation_audited_revision; Type: FK CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER TABLE ONLY marker_moderation_audited
    ADD CONSTRAINT fk_marker_moderation_audited_revision FOREIGN KEY (revision) REFERENCES revision(id);


--
-- Name: fk_motive_audited_revision; Type: FK CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER TABLE ONLY motive_audited
    ADD CONSTRAINT fk_motive_audited_revision FOREIGN KEY (revision) REFERENCES revision(id);


--
-- Name: fk_motive_marker_moderation_audited_revision; Type: FK CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER TABLE ONLY motive_marker_moderation_audited
    ADD CONSTRAINT fk_motive_marker_moderation_audited_revision FOREIGN KEY (revision) REFERENCES revision(id);


--
-- Name: fk_tool_audited_revision; Type: FK CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER TABLE ONLY tool_audited
    ADD CONSTRAINT fk_tool_audited_revision FOREIGN KEY (revision) REFERENCES revision(id);


--
-- Name: fk_user_audited_revision; Type: FK CONSTRAINT; Schema: geocab_auditoria; Owner: geocab_auditoria
--

ALTER TABLE ONLY user_audited
    ADD CONSTRAINT fk_user_audited_revision FOREIGN KEY (revision) REFERENCES revision(id);


--
-- Name: geocab; Type: ACL; Schema: -; Owner: geocab
--

REVOKE ALL ON SCHEMA geocab FROM PUBLIC;
REVOKE ALL ON SCHEMA geocab FROM geocab;
GRANT ALL ON SCHEMA geocab TO geocab;
GRANT USAGE ON SCHEMA geocab TO gr_geocab_acesso;
GRANT USAGE ON SCHEMA geocab TO gr_geocab_consulta;


--
-- Name: geocab_auditoria; Type: ACL; Schema: -; Owner: geocab_auditoria
--

REVOKE ALL ON SCHEMA geocab_auditoria FROM PUBLIC;
REVOKE ALL ON SCHEMA geocab_auditoria FROM geocab_auditoria;
GRANT ALL ON SCHEMA geocab_auditoria TO geocab_auditoria;
GRANT USAGE ON SCHEMA geocab_auditoria TO gr_geocab_auditoria_consulta;
GRANT USAGE ON SCHEMA geocab_auditoria TO gr_geocab_auditoria_acesso;


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


SET search_path = geocab, pg_catalog;

--
-- Name: access_group; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON TABLE access_group FROM PUBLIC;
REVOKE ALL ON TABLE access_group FROM geocab;
GRANT ALL ON TABLE access_group TO geocab;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE access_group TO gr_geocab_acesso;
GRANT SELECT ON TABLE access_group TO gr_geocab_consulta;


--
-- Name: access_group_custom_search; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON TABLE access_group_custom_search FROM PUBLIC;
REVOKE ALL ON TABLE access_group_custom_search FROM geocab;
GRANT ALL ON TABLE access_group_custom_search TO geocab;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE access_group_custom_search TO gr_geocab_acesso;
GRANT SELECT ON TABLE access_group_custom_search TO gr_geocab_consulta;


--
-- Name: access_group_layer; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON TABLE access_group_layer FROM PUBLIC;
REVOKE ALL ON TABLE access_group_layer FROM geocab;
GRANT ALL ON TABLE access_group_layer TO geocab;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE access_group_layer TO gr_geocab_acesso;
GRANT SELECT ON TABLE access_group_layer TO gr_geocab_consulta;


--
-- Name: access_group_tool; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON TABLE access_group_tool FROM PUBLIC;
REVOKE ALL ON TABLE access_group_tool FROM geocab;
GRANT ALL ON TABLE access_group_tool TO geocab;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE access_group_tool TO gr_geocab_acesso;
GRANT SELECT ON TABLE access_group_tool TO gr_geocab_consulta;


--
-- Name: access_group_tool_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON SEQUENCE access_group_tool_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE access_group_tool_id_seq FROM geocab;
GRANT ALL ON SEQUENCE access_group_tool_id_seq TO geocab;
GRANT ALL ON SEQUENCE access_group_tool_id_seq TO gr_geocab_acesso;
GRANT SELECT ON SEQUENCE access_group_tool_id_seq TO gr_geocab_consulta;


--
-- Name: access_group_user; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON TABLE access_group_user FROM PUBLIC;
REVOKE ALL ON TABLE access_group_user FROM geocab;
GRANT ALL ON TABLE access_group_user TO geocab;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE access_group_user TO gr_geocab_acesso;
GRANT SELECT ON TABLE access_group_user TO gr_geocab_consulta;


--
-- Name: access_group_user_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON SEQUENCE access_group_user_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE access_group_user_id_seq FROM geocab;
GRANT ALL ON SEQUENCE access_group_user_id_seq TO geocab;
GRANT ALL ON SEQUENCE access_group_user_id_seq TO gr_geocab_acesso;
GRANT SELECT ON SEQUENCE access_group_user_id_seq TO gr_geocab_consulta;


--
-- Name: attribute; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON TABLE attribute FROM PUBLIC;
REVOKE ALL ON TABLE attribute FROM geocab;
GRANT ALL ON TABLE attribute TO geocab;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE attribute TO gr_geocab_acesso;
GRANT SELECT ON TABLE attribute TO gr_geocab_consulta;


--
-- Name: attribute_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON SEQUENCE attribute_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE attribute_id_seq FROM geocab;
GRANT ALL ON SEQUENCE attribute_id_seq TO geocab;
GRANT ALL ON SEQUENCE attribute_id_seq TO gr_geocab_acesso;
GRANT SELECT ON SEQUENCE attribute_id_seq TO gr_geocab_consulta;


--
-- Name: custom_search; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON TABLE custom_search FROM PUBLIC;
REVOKE ALL ON TABLE custom_search FROM geocab;
GRANT ALL ON TABLE custom_search TO geocab;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE custom_search TO gr_geocab_acesso;
GRANT SELECT ON TABLE custom_search TO gr_geocab_consulta;


--
-- Name: data_source; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON TABLE data_source FROM PUBLIC;
REVOKE ALL ON TABLE data_source FROM geocab;
GRANT ALL ON TABLE data_source TO geocab;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE data_source TO gr_geocab_acesso;
GRANT SELECT ON TABLE data_source TO gr_geocab_consulta;


--
-- Name: data_source_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON SEQUENCE data_source_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE data_source_id_seq FROM geocab;
GRANT ALL ON SEQUENCE data_source_id_seq TO geocab;
GRANT ALL ON SEQUENCE data_source_id_seq TO gr_geocab_acesso;
GRANT SELECT ON SEQUENCE data_source_id_seq TO gr_geocab_consulta;


--
-- Name: layer; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON TABLE layer FROM PUBLIC;
REVOKE ALL ON TABLE layer FROM geocab;
GRANT ALL ON TABLE layer TO geocab;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE layer TO gr_geocab_acesso;
GRANT SELECT ON TABLE layer TO gr_geocab_consulta;


--
-- Name: layer_field; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON TABLE layer_field FROM PUBLIC;
REVOKE ALL ON TABLE layer_field FROM geocab;
GRANT ALL ON TABLE layer_field TO geocab;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE layer_field TO gr_geocab_acesso;
GRANT SELECT ON TABLE layer_field TO gr_geocab_consulta;


--
-- Name: layer_group; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON TABLE layer_group FROM PUBLIC;
REVOKE ALL ON TABLE layer_group FROM geocab;
GRANT ALL ON TABLE layer_group TO geocab;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE layer_group TO gr_geocab_acesso;
GRANT SELECT ON TABLE layer_group TO gr_geocab_consulta;


--
-- Name: layer_group_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON SEQUENCE layer_group_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE layer_group_id_seq FROM geocab;
GRANT ALL ON SEQUENCE layer_group_id_seq TO geocab;
GRANT ALL ON SEQUENCE layer_group_id_seq TO gr_geocab_acesso;
GRANT SELECT ON SEQUENCE layer_group_id_seq TO gr_geocab_consulta;


--
-- Name: layer_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON SEQUENCE layer_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE layer_id_seq FROM geocab;
GRANT ALL ON SEQUENCE layer_id_seq TO geocab;
GRANT ALL ON SEQUENCE layer_id_seq TO gr_geocab_acesso;
GRANT SELECT ON SEQUENCE layer_id_seq TO gr_geocab_consulta;


--
-- Name: marker; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON TABLE marker FROM PUBLIC;
REVOKE ALL ON TABLE marker FROM geocab;
GRANT ALL ON TABLE marker TO geocab;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE marker TO gr_geocab_acesso;
GRANT SELECT ON TABLE marker TO gr_geocab_consulta;


--
-- Name: marker_attribute; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON TABLE marker_attribute FROM PUBLIC;
REVOKE ALL ON TABLE marker_attribute FROM geocab;
GRANT ALL ON TABLE marker_attribute TO geocab;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE marker_attribute TO gr_geocab_acesso;
GRANT SELECT ON TABLE marker_attribute TO gr_geocab_consulta;


--
-- Name: marker_attribute_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON SEQUENCE marker_attribute_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE marker_attribute_id_seq FROM geocab;
GRANT ALL ON SEQUENCE marker_attribute_id_seq TO geocab;
GRANT ALL ON SEQUENCE marker_attribute_id_seq TO gr_geocab_acesso;
GRANT SELECT ON SEQUENCE marker_attribute_id_seq TO gr_geocab_consulta;


--
-- Name: marker_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON SEQUENCE marker_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE marker_id_seq FROM geocab;
GRANT ALL ON SEQUENCE marker_id_seq TO geocab;
GRANT ALL ON SEQUENCE marker_id_seq TO gr_geocab_acesso;
GRANT SELECT ON SEQUENCE marker_id_seq TO gr_geocab_consulta;


--
-- Name: marker_moderation; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON TABLE marker_moderation FROM PUBLIC;
REVOKE ALL ON TABLE marker_moderation FROM geocab;
GRANT ALL ON TABLE marker_moderation TO geocab;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE marker_moderation TO gr_geocab_acesso;
GRANT SELECT ON TABLE marker_moderation TO gr_geocab_consulta;


--
-- Name: motive; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON TABLE motive FROM PUBLIC;
REVOKE ALL ON TABLE motive FROM geocab;
GRANT ALL ON TABLE motive TO geocab;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE motive TO gr_geocab_acesso;
GRANT SELECT ON TABLE motive TO gr_geocab_consulta;


--
-- Name: motive_marker_moderation; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON TABLE motive_marker_moderation FROM PUBLIC;
REVOKE ALL ON TABLE motive_marker_moderation FROM geocab;
GRANT ALL ON TABLE motive_marker_moderation TO geocab;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE motive_marker_moderation TO gr_geocab_acesso;
GRANT SELECT ON TABLE motive_marker_moderation TO gr_geocab_consulta;


--
-- Name: tool; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON TABLE tool FROM PUBLIC;
REVOKE ALL ON TABLE tool FROM geocab;
GRANT ALL ON TABLE tool TO geocab;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE tool TO gr_geocab_acesso;
GRANT SELECT ON TABLE tool TO gr_geocab_consulta;


--
-- Name: user; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON TABLE "user" FROM PUBLIC;
REVOKE ALL ON TABLE "user" FROM geocab;
GRANT ALL ON TABLE "user" TO geocab;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE "user" TO gr_geocab_acesso;
GRANT SELECT ON TABLE "user" TO gr_geocab_consulta;


--
-- Name: user_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON SEQUENCE user_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE user_id_seq FROM geocab;
GRANT ALL ON SEQUENCE user_id_seq TO geocab;
GRANT ALL ON SEQUENCE user_id_seq TO gr_geocab_acesso;
GRANT SELECT ON SEQUENCE user_id_seq TO gr_geocab_consulta;


--
-- Name: user_social_connection; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON TABLE user_social_connection FROM PUBLIC;
REVOKE ALL ON TABLE user_social_connection FROM geocab;
GRANT ALL ON TABLE user_social_connection TO geocab;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE user_social_connection TO gr_geocab_acesso;
GRANT SELECT ON TABLE user_social_connection TO gr_geocab_consulta;


--
-- Name: user_social_connection_id_seq; Type: ACL; Schema: geocab; Owner: geocab
--

REVOKE ALL ON SEQUENCE user_social_connection_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE user_social_connection_id_seq FROM geocab;
GRANT ALL ON SEQUENCE user_social_connection_id_seq TO geocab;
GRANT ALL ON SEQUENCE user_social_connection_id_seq TO gr_geocab_acesso;
GRANT SELECT ON SEQUENCE user_social_connection_id_seq TO gr_geocab_consulta;


SET search_path = geocab_auditoria, pg_catalog;

--
-- Name: access_group_audited; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON TABLE access_group_audited FROM PUBLIC;
REVOKE ALL ON TABLE access_group_audited FROM geocab_auditoria;
GRANT ALL ON TABLE access_group_audited TO geocab_auditoria;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE access_group_audited TO gr_geocab_auditoria_acesso;
GRANT SELECT ON TABLE access_group_audited TO gr_geocab_auditoria_consulta;


--
-- Name: access_group_custom_search_audited; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON TABLE access_group_custom_search_audited FROM PUBLIC;
REVOKE ALL ON TABLE access_group_custom_search_audited FROM geocab_auditoria;
GRANT ALL ON TABLE access_group_custom_search_audited TO geocab_auditoria;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE access_group_custom_search_audited TO gr_geocab_auditoria_acesso;
GRANT SELECT ON TABLE access_group_custom_search_audited TO gr_geocab_auditoria_consulta;


--
-- Name: access_group_layer_audited; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON TABLE access_group_layer_audited FROM PUBLIC;
REVOKE ALL ON TABLE access_group_layer_audited FROM geocab_auditoria;
GRANT ALL ON TABLE access_group_layer_audited TO geocab_auditoria;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE access_group_layer_audited TO gr_geocab_auditoria_acesso;
GRANT SELECT ON TABLE access_group_layer_audited TO gr_geocab_auditoria_consulta;


--
-- Name: access_group_tool_audited; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON TABLE access_group_tool_audited FROM PUBLIC;
REVOKE ALL ON TABLE access_group_tool_audited FROM geocab_auditoria;
GRANT ALL ON TABLE access_group_tool_audited TO geocab_auditoria;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE access_group_tool_audited TO gr_geocab_auditoria_acesso;
GRANT SELECT ON TABLE access_group_tool_audited TO gr_geocab_auditoria_consulta;


--
-- Name: access_group_user_audited; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON TABLE access_group_user_audited FROM PUBLIC;
REVOKE ALL ON TABLE access_group_user_audited FROM geocab_auditoria;
GRANT ALL ON TABLE access_group_user_audited TO geocab_auditoria;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE access_group_user_audited TO gr_geocab_auditoria_acesso;
GRANT SELECT ON TABLE access_group_user_audited TO gr_geocab_auditoria_consulta;


--
-- Name: attribute_audited; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON TABLE attribute_audited FROM PUBLIC;
REVOKE ALL ON TABLE attribute_audited FROM geocab_auditoria;
GRANT ALL ON TABLE attribute_audited TO geocab_auditoria;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE attribute_audited TO gr_geocab_auditoria_acesso;
GRANT SELECT ON TABLE attribute_audited TO gr_geocab_auditoria_consulta;


--
-- Name: custom_search_audited; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON TABLE custom_search_audited FROM PUBLIC;
REVOKE ALL ON TABLE custom_search_audited FROM geocab_auditoria;
GRANT ALL ON TABLE custom_search_audited TO geocab_auditoria;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE custom_search_audited TO gr_geocab_auditoria_acesso;
GRANT SELECT ON TABLE custom_search_audited TO gr_geocab_auditoria_consulta;


--
-- Name: custom_search_layer_field_audited; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON TABLE custom_search_layer_field_audited FROM PUBLIC;
REVOKE ALL ON TABLE custom_search_layer_field_audited FROM geocab_auditoria;
GRANT ALL ON TABLE custom_search_layer_field_audited TO geocab_auditoria;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE custom_search_layer_field_audited TO gr_geocab_auditoria_acesso;
GRANT SELECT ON TABLE custom_search_layer_field_audited TO gr_geocab_auditoria_consulta;


--
-- Name: data_source_audited; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON TABLE data_source_audited FROM PUBLIC;
REVOKE ALL ON TABLE data_source_audited FROM geocab_auditoria;
GRANT ALL ON TABLE data_source_audited TO geocab_auditoria;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE data_source_audited TO gr_geocab_auditoria_acesso;
GRANT SELECT ON TABLE data_source_audited TO gr_geocab_auditoria_consulta;


--
-- Name: field_layer_audited; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON TABLE field_layer_audited FROM PUBLIC;
REVOKE ALL ON TABLE field_layer_audited FROM geocab_auditoria;
GRANT ALL ON TABLE field_layer_audited TO geocab_auditoria;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE field_layer_audited TO gr_geocab_auditoria_acesso;
GRANT SELECT ON TABLE field_layer_audited TO gr_geocab_auditoria_consulta;


--
-- Name: layer_audited; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON TABLE layer_audited FROM PUBLIC;
REVOKE ALL ON TABLE layer_audited FROM geocab_auditoria;
GRANT ALL ON TABLE layer_audited TO geocab_auditoria;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE layer_audited TO gr_geocab_auditoria_acesso;
GRANT SELECT ON TABLE layer_audited TO gr_geocab_auditoria_consulta;


--
-- Name: layer_field_audited; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON TABLE layer_field_audited FROM PUBLIC;
REVOKE ALL ON TABLE layer_field_audited FROM geocab_auditoria;
GRANT ALL ON TABLE layer_field_audited TO geocab_auditoria;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE layer_field_audited TO gr_geocab_auditoria_acesso;
GRANT SELECT ON TABLE layer_field_audited TO gr_geocab_auditoria_consulta;


--
-- Name: layer_group_audited; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON TABLE layer_group_audited FROM PUBLIC;
REVOKE ALL ON TABLE layer_group_audited FROM geocab_auditoria;
GRANT ALL ON TABLE layer_group_audited TO geocab_auditoria;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE layer_group_audited TO gr_geocab_auditoria_acesso;
GRANT SELECT ON TABLE layer_group_audited TO gr_geocab_auditoria_consulta;


--
-- Name: layer_group_layer_audited; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON TABLE layer_group_layer_audited FROM PUBLIC;
REVOKE ALL ON TABLE layer_group_layer_audited FROM geocab_auditoria;
GRANT ALL ON TABLE layer_group_layer_audited TO geocab_auditoria;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE layer_group_layer_audited TO gr_geocab_auditoria_acesso;
GRANT SELECT ON TABLE layer_group_layer_audited TO gr_geocab_auditoria_consulta;


--
-- Name: layer_group_layer_group_audited; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON TABLE layer_group_layer_group_audited FROM PUBLIC;
REVOKE ALL ON TABLE layer_group_layer_group_audited FROM geocab_auditoria;
GRANT ALL ON TABLE layer_group_layer_group_audited TO geocab_auditoria;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE layer_group_layer_group_audited TO gr_geocab_auditoria_acesso;
GRANT SELECT ON TABLE layer_group_layer_group_audited TO gr_geocab_auditoria_consulta;


--
-- Name: marker_attribute_audited; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON TABLE marker_attribute_audited FROM PUBLIC;
REVOKE ALL ON TABLE marker_attribute_audited FROM geocab_auditoria;
GRANT ALL ON TABLE marker_attribute_audited TO geocab_auditoria;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE marker_attribute_audited TO gr_geocab_auditoria_acesso;
GRANT SELECT ON TABLE marker_attribute_audited TO gr_geocab_auditoria_consulta;


--
-- Name: marker_audited; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON TABLE marker_audited FROM PUBLIC;
REVOKE ALL ON TABLE marker_audited FROM geocab_auditoria;
GRANT ALL ON TABLE marker_audited TO geocab_auditoria;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE marker_audited TO gr_geocab_auditoria_acesso;
GRANT SELECT ON TABLE marker_audited TO gr_geocab_auditoria_consulta;


--
-- Name: marker_moderation_audited; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON TABLE marker_moderation_audited FROM PUBLIC;
REVOKE ALL ON TABLE marker_moderation_audited FROM geocab_auditoria;
GRANT ALL ON TABLE marker_moderation_audited TO geocab_auditoria;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE marker_moderation_audited TO gr_geocab_auditoria_acesso;
GRANT SELECT ON TABLE marker_moderation_audited TO gr_geocab_auditoria_consulta;


--
-- Name: motive_audited; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON TABLE motive_audited FROM PUBLIC;
REVOKE ALL ON TABLE motive_audited FROM geocab_auditoria;
GRANT ALL ON TABLE motive_audited TO geocab_auditoria;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE motive_audited TO gr_geocab_auditoria_acesso;
GRANT SELECT ON TABLE motive_audited TO gr_geocab_auditoria_consulta;


--
-- Name: motive_marker_moderation_audited; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON TABLE motive_marker_moderation_audited FROM PUBLIC;
REVOKE ALL ON TABLE motive_marker_moderation_audited FROM geocab_auditoria;
GRANT ALL ON TABLE motive_marker_moderation_audited TO geocab_auditoria;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE motive_marker_moderation_audited TO gr_geocab_auditoria_acesso;
GRANT SELECT ON TABLE motive_marker_moderation_audited TO gr_geocab_auditoria_consulta;


--
-- Name: revision; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON TABLE revision FROM PUBLIC;
REVOKE ALL ON TABLE revision FROM geocab_auditoria;
GRANT ALL ON TABLE revision TO geocab_auditoria;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE revision TO gr_geocab_auditoria_acesso;
GRANT SELECT ON TABLE revision TO gr_geocab_auditoria_consulta;


--
-- Name: revision_id_seq; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON SEQUENCE revision_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE revision_id_seq FROM geocab_auditoria;
GRANT ALL ON SEQUENCE revision_id_seq TO geocab_auditoria;
GRANT ALL ON SEQUENCE revision_id_seq TO gr_geocab_auditoria_acesso;
GRANT SELECT ON SEQUENCE revision_id_seq TO gr_geocab_auditoria_consulta;


--
-- Name: tool_audited; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON TABLE tool_audited FROM PUBLIC;
REVOKE ALL ON TABLE tool_audited FROM geocab_auditoria;
GRANT ALL ON TABLE tool_audited TO geocab_auditoria;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE tool_audited TO gr_geocab_auditoria_acesso;
GRANT SELECT ON TABLE tool_audited TO gr_geocab_auditoria_consulta;


--
-- Name: user_audited; Type: ACL; Schema: geocab_auditoria; Owner: geocab_auditoria
--

REVOKE ALL ON TABLE user_audited FROM PUBLIC;
REVOKE ALL ON TABLE user_audited FROM geocab_auditoria;
GRANT ALL ON TABLE user_audited TO geocab_auditoria;
GRANT SELECT,INSERT,DELETE,TRUNCATE,UPDATE ON TABLE user_audited TO gr_geocab_auditoria_acesso;
GRANT SELECT ON TABLE user_audited TO gr_geocab_auditoria_consulta;


--
-- PostgreSQL database dump complete
--

