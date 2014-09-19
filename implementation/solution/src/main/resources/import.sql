--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = geocab, pg_catalog;

--DEFAULT Users
INSERT INTO geocab.user(id, created, email, name, password, enabled, role) VALUES (1, now(), 'admin@geocab.com.br', 'Administrador de Sistemas', '92148f305c6b896aced78fd57d7526c8c38fb08c', true, 0);
SELECT pg_catalog.setval('user_id_seq', 2, true);