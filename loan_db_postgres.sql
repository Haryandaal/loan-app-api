--
-- PostgreSQL database dump
--

-- Dumped from database version 14.13 (Ubuntu 14.13-0ubuntu0.22.04.1)
-- Dumped by pg_dump version 14.13 (Ubuntu 14.13-0ubuntu0.22.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: customer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.customer (
    id character varying(255) NOT NULL,
    address character varying(255) NOT NULL,
    credit_score double precision,
    email character varying(255) NOT NULL,
    employment_status character varying(255),
    income double precision NOT NULL,
    name character varying(255) NOT NULL,
    phone_number character varying(255),
    user_account_id character varying(255)
);


ALTER TABLE public.customer OWNER TO postgres;

--
-- Name: loan; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.loan (
    id character varying(255) NOT NULL,
    amount bigint,
    application_date timestamp(6) without time zone,
    approval_status character varying(255),
    duration_in_months integer,
    interest_rate double precision,
    rejection_reason character varying(255),
    customer_id character varying(255),
    CONSTRAINT loan_approval_status_check CHECK (((approval_status)::text = ANY ((ARRAY['PENDING'::character varying, 'APPROVED'::character varying, 'REJECTED'::character varying])::text[])))
);


ALTER TABLE public.loan OWNER TO postgres;

--
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
    id character varying(255) NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- Name: user_account; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_account (
    id character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    username character varying(255) NOT NULL,
    role_id character varying(255) NOT NULL
);


ALTER TABLE public.user_account OWNER TO postgres;

--
-- Data for Name: customer; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.customer (id, address, credit_score, email, employment_status, income, name, phone_number, user_account_id) FROM stdin;
7d54ffa2-37b7-46a9-a0fb-3b8d898dc6e8	Jl. Melati Sari No. 20 Kelapa Gading	600	bani@example.com	CONTRACT	5000000	Banii	08994811111	c74ffa25-8e59-4219-8a73-dc5e68f1f3a0
\.


--
-- Data for Name: loan; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.loan (id, amount, application_date, approval_status, duration_in_months, interest_rate, rejection_reason, customer_id) FROM stdin;
6b9060f3-c517-42db-a5ed-b3fe5add73ca	500000	2024-11-17 16:33:21.515584	APPROVED	2	5.5	\N	7d54ffa2-37b7-46a9-a0fb-3b8d898dc6e8
\.


--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roles (id, name) FROM stdin;
7df23221-cb7d-43ae-9a40-c45abd359f23	Admin
f16701e0-7fb5-47c3-a209-4560cc0cd61a	Customer
\.


--
-- Data for Name: user_account; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_account (id, password, username, role_id) FROM stdin;
69a31312-89d8-43e2-96ef-81654d965f1a	$2a$10$RqIT3.kKCcuuBGxL8YCTRu4jq/d/irjcKUowFTz7DAZEgEdP28QOC	admin	7df23221-cb7d-43ae-9a40-c45abd359f23
c74ffa25-8e59-4219-8a73-dc5e68f1f3a0	$2a$10$rk3Uwu3PU1TES2Czojcb1.WHgcLvLA6ZnVDHdNexjrQLtARCLzIZC	Bani	f16701e0-7fb5-47c3-a209-4560cc0cd61a
\.


--
-- Name: customer customer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (id);


--
-- Name: loan loan_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loan
    ADD CONSTRAINT loan_pkey PRIMARY KEY (id);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: customer uk4qm52n85kwhmxj2nktmj1kv9h; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT uk4qm52n85kwhmxj2nktmj1kv9h UNIQUE (user_account_id);


--
-- Name: user_account ukcastjbvpeeus0r8lbpehiu0e4; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_account
    ADD CONSTRAINT ukcastjbvpeeus0r8lbpehiu0e4 UNIQUE (username);


--
-- Name: customer ukdwk6cx0afu8bs9o4t536v1j5v; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT ukdwk6cx0afu8bs9o4t536v1j5v UNIQUE (email);


--
-- Name: roles ukofx66keruapi6vyqpv6f2or37; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT ukofx66keruapi6vyqpv6f2or37 UNIQUE (name);


--
-- Name: customer ukrosd2guvs3i1agkplv5n8vu82; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT ukrosd2guvs3i1agkplv5n8vu82 UNIQUE (phone_number);


--
-- Name: user_account user_account_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_account
    ADD CONSTRAINT user_account_pkey PRIMARY KEY (id);


--
-- Name: user_account fk7km135byyeyxob1l345ptmxc5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_account
    ADD CONSTRAINT fk7km135byyeyxob1l345ptmxc5 FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- Name: loan fkcwv05agfqwg5ndy6adbefsx7d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.loan
    ADD CONSTRAINT fkcwv05agfqwg5ndy6adbefsx7d FOREIGN KEY (customer_id) REFERENCES public.customer(id);


--
-- Name: customer fkpjmlws4d21g1v76mthbwc4v79; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT fkpjmlws4d21g1v76mthbwc4v79 FOREIGN KEY (user_account_id) REFERENCES public.user_account(id);


--
-- PostgreSQL database dump complete
--

