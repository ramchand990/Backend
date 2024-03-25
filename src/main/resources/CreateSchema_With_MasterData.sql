--
-- PostgreSQL database dump
--

-- Dumped from database version 14.5
-- Dumped by pg_dump version 14.2

-- Started on 2022-12-29 15:31:01

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

--
-- TOC entry 5 (class 2615 OID 27701)
-- Name: healspan; Type: SCHEMA; Schema: -; Owner: root
--

CREATE SCHEMA healspan;


ALTER SCHEMA healspan OWNER TO root;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 209 (class 1259 OID 27702)
-- Name: chronic_illness_mst; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.chronic_illness_mst (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE healspan.chronic_illness_mst OWNER TO root;

--
-- TOC entry 237 (class 1259 OID 27858)
-- Name: chronic_illness_mst_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.chronic_illness_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.chronic_illness_mst_id_seq OWNER TO root;

--
-- TOC entry 210 (class 1259 OID 27707)
-- Name: claim_assignment; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.claim_assignment (
    id bigint NOT NULL,
    action_taken character varying(255),
    assigned_date_time timestamp without time zone,
    assigned_comments character varying(255),
    completed_date_time timestamp without time zone,
    claim_stage_link_id bigint,
    user_mst_id bigint
);


ALTER TABLE healspan.claim_assignment OWNER TO root;

--
-- TOC entry 238 (class 1259 OID 27859)
-- Name: claim_assignment_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.claim_assignment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.claim_assignment_id_seq OWNER TO root;

--
-- TOC entry 211 (class 1259 OID 27714)
-- Name: claim_info; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.claim_info (
    id bigint NOT NULL,
    created_date_time timestamp without time zone,
    tpa_claim_id character varying(255),
    hospital_mst_id bigint,
    user_mst_id bigint,
    last_updated_date_time timestamp without time zone
);


ALTER TABLE healspan.claim_info OWNER TO root;

--
-- TOC entry 239 (class 1259 OID 27860)
-- Name: claim_info_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.claim_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.claim_info_id_seq OWNER TO root;

--
-- TOC entry 212 (class 1259 OID 27719)
-- Name: claim_stage_link; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.claim_stage_link (
    id bigint NOT NULL,
    created_date_time timestamp without time zone,
    claim_info_id bigint,
    claim_stage_mst_id bigint,
    insurance_info_id bigint,
    medical_info_id bigint,
    patient_info_id bigint,
    status_mst_id bigint,
    user_mst_id bigint,
    last_updated_date_time timestamp without time zone
);


ALTER TABLE healspan.claim_stage_link OWNER TO root;

--
-- TOC entry 240 (class 1259 OID 27861)
-- Name: claim_stage_link_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.claim_stage_link_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.claim_stage_link_id_seq OWNER TO root;

--
-- TOC entry 213 (class 1259 OID 27724)
-- Name: claim_stage_mst; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.claim_stage_mst (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE healspan.claim_stage_mst OWNER TO root;

--
-- TOC entry 241 (class 1259 OID 27862)
-- Name: claim_stage_mst_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.claim_stage_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.claim_stage_mst_id_seq OWNER TO root;

--
-- TOC entry 265 (class 1259 OID 28059)
-- Name: diagnosis_mst; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.diagnosis_mst (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE healspan.diagnosis_mst OWNER TO root;

--
-- TOC entry 242 (class 1259 OID 27863)
-- Name: diagnosis_mst_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.diagnosis_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.diagnosis_mst_id_seq OWNER TO root;

--
-- TOC entry 214 (class 1259 OID 27729)
-- Name: diagnosis_mst_old; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.diagnosis_mst_old (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE healspan.diagnosis_mst_old OWNER TO root;

--
-- TOC entry 215 (class 1259 OID 27734)
-- Name: document; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.document (
    id bigint NOT NULL,
    name character varying(255),
    path character varying(255),
    type character varying(255),
    status character varying(255),
    document_type_mst_id bigint,
    medical_info_id bigint
);


ALTER TABLE healspan.document OWNER TO root;

--
-- TOC entry 243 (class 1259 OID 27864)
-- Name: document_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.document_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.document_id_seq OWNER TO root;

--
-- TOC entry 216 (class 1259 OID 27741)
-- Name: document_type_mst; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.document_type_mst (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE healspan.document_type_mst OWNER TO root;

--
-- TOC entry 244 (class 1259 OID 27865)
-- Name: document_type_mst_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.document_type_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.document_type_mst_id_seq OWNER TO root;

--
-- TOC entry 217 (class 1259 OID 27746)
-- Name: gender_mst; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.gender_mst (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE healspan.gender_mst OWNER TO root;

--
-- TOC entry 245 (class 1259 OID 27866)
-- Name: gender_mst_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.gender_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.gender_mst_id_seq OWNER TO root;

--
-- TOC entry 218 (class 1259 OID 27751)
-- Name: hospital_mst; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.hospital_mst (
    id bigint NOT NULL,
    uhid character varying(255),
    name character varying(255)
);


ALTER TABLE healspan.hospital_mst OWNER TO root;

--
-- TOC entry 246 (class 1259 OID 27867)
-- Name: hospital_mst_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.hospital_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.hospital_mst_id_seq OWNER TO root;

--
-- TOC entry 219 (class 1259 OID 27758)
-- Name: insurance_company_mst; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.insurance_company_mst (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE healspan.insurance_company_mst OWNER TO root;

--
-- TOC entry 247 (class 1259 OID 27868)
-- Name: insurance_company_mst_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.insurance_company_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.insurance_company_mst_id_seq OWNER TO root;

--
-- TOC entry 220 (class 1259 OID 27763)
-- Name: insurance_info; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.insurance_info (
    id bigint NOT NULL,
    approval_amount_at_discharge double precision,
    approval_enhancement_amount double precision,
    claim_id_pre_auth_number character varying(255),
    group_company character varying(255),
    initial_approval_limit double precision,
    is_group_policy boolean,
    policy_holder_name character varying(255),
    policy_number character varying(255),
    tpa_number character varying(255),
    insurance_company_mst_id bigint,
    relationship_mst_id bigint,
    tpa_mst_id bigint
);


ALTER TABLE healspan.insurance_info OWNER TO root;

--
-- TOC entry 248 (class 1259 OID 27869)
-- Name: insurance_info_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.insurance_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.insurance_info_id_seq OWNER TO root;

--
-- TOC entry 221 (class 1259 OID 27770)
-- Name: mandatory_documents_mst; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.mandatory_documents_mst (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE healspan.mandatory_documents_mst OWNER TO root;

--
-- TOC entry 249 (class 1259 OID 27870)
-- Name: mandatory_documents_mst_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.mandatory_documents_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.mandatory_documents_mst_id_seq OWNER TO root;

--
-- TOC entry 222 (class 1259 OID 27775)
-- Name: medical_chronic_illness_link; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.medical_chronic_illness_link (
    id bigint NOT NULL,
    chronic_illness_mst_id bigint,
    medical_info_id bigint
);


ALTER TABLE healspan.medical_chronic_illness_link OWNER TO root;

--
-- TOC entry 250 (class 1259 OID 27871)
-- Name: medical_chronic_illness_link_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.medical_chronic_illness_link_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.medical_chronic_illness_link_id_seq OWNER TO root;

--
-- TOC entry 223 (class 1259 OID 27780)
-- Name: medical_info; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.medical_info (
    id bigint NOT NULL,
    date_of_first_diagnosis timestamp without time zone,
    doctor_name character varying(255),
    doctor_qualification character varying(255),
    doctor_registration_number character varying(255),
    past_chronic_illness character varying(255),
    diagnosis_mst_id bigint,
    procedure_mst_id bigint,
    speciality_mst_id bigint,
    treatment_type_mst_id bigint
);


ALTER TABLE healspan.medical_info OWNER TO root;

--
-- TOC entry 251 (class 1259 OID 27872)
-- Name: medical_info_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.medical_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.medical_info_id_seq OWNER TO root;

--
-- TOC entry 224 (class 1259 OID 27787)
-- Name: other_costs_mst; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.other_costs_mst (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE healspan.other_costs_mst OWNER TO root;

--
-- TOC entry 252 (class 1259 OID 27873)
-- Name: other_costs_mst_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.other_costs_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.other_costs_mst_id_seq OWNER TO root;

--
-- TOC entry 225 (class 1259 OID 27792)
-- Name: patient_info; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.patient_info (
    id bigint NOT NULL,
    age integer,
    bill_number character varying(255),
    claimed_amount double precision,
    cost_per_day double precision,
    date_of_birth timestamp without time zone,
    date_of_admission timestamp without time zone,
    date_of_discharge timestamp without time zone,
    enhancement_estimation double precision,
    estimated_date_of_discharge timestamp without time zone,
    final_bill_amount double precision,
    first_name character varying(255),
    hospital_uhid character varying(255),
    initial_costs_estimation double precision,
    is_primary_insured boolean,
    last_name character varying(255),
    middle_name character varying(255),
    mobile_no character varying(255),
    other_costs_estimation double precision,
    total_room_cost double precision,
    gender_mst_id bigint,
    hospital_mst_id bigint,
    other_costs_mst_id bigint,
    procedure_mst_id bigint,
    room_category_mst_id bigint
);


ALTER TABLE healspan.patient_info OWNER TO root;

--
-- TOC entry 253 (class 1259 OID 27874)
-- Name: patient_info_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.patient_info_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.patient_info_id_seq OWNER TO root;

--
-- TOC entry 226 (class 1259 OID 27799)
-- Name: patient_othercost_link; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.patient_othercost_link (
    id bigint NOT NULL,
    amount double precision,
    other_costs_mst_id bigint,
    patient_info_id bigint
);


ALTER TABLE healspan.patient_othercost_link OWNER TO root;

--
-- TOC entry 254 (class 1259 OID 27875)
-- Name: patient_othercost_link_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.patient_othercost_link_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.patient_othercost_link_id_seq OWNER TO root;

--
-- TOC entry 227 (class 1259 OID 27804)
-- Name: procedure_mst; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.procedure_mst (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE healspan.procedure_mst OWNER TO root;

--
-- TOC entry 255 (class 1259 OID 27876)
-- Name: procedure_mst_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.procedure_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.procedure_mst_id_seq OWNER TO root;

--
-- TOC entry 228 (class 1259 OID 27809)
-- Name: question_answer; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.question_answer (
    id bigint NOT NULL,
    answers character varying(255),
    questions character varying(255),
    medical_info_id bigint
);


ALTER TABLE healspan.question_answer OWNER TO root;

--
-- TOC entry 256 (class 1259 OID 27877)
-- Name: question_answer_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.question_answer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.question_answer_id_seq OWNER TO root;

--
-- TOC entry 229 (class 1259 OID 27816)
-- Name: relationship_mst; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.relationship_mst (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE healspan.relationship_mst OWNER TO root;

--
-- TOC entry 257 (class 1259 OID 27878)
-- Name: relationship_mst_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.relationship_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.relationship_mst_id_seq OWNER TO root;

--
-- TOC entry 230 (class 1259 OID 27821)
-- Name: room_category_mst; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.room_category_mst (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE healspan.room_category_mst OWNER TO root;

--
-- TOC entry 258 (class 1259 OID 27879)
-- Name: room_category_mst_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.room_category_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.room_category_mst_id_seq OWNER TO root;

--
-- TOC entry 231 (class 1259 OID 27826)
-- Name: speciality_mst; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.speciality_mst (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE healspan.speciality_mst OWNER TO root;

--
-- TOC entry 259 (class 1259 OID 27880)
-- Name: speciality_mst_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.speciality_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.speciality_mst_id_seq OWNER TO root;

--
-- TOC entry 232 (class 1259 OID 27831)
-- Name: status_mst; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.status_mst (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE healspan.status_mst OWNER TO root;

--
-- TOC entry 260 (class 1259 OID 27881)
-- Name: status_mst_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.status_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.status_mst_id_seq OWNER TO root;

--
-- TOC entry 233 (class 1259 OID 27836)
-- Name: tpa_mst; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.tpa_mst (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE healspan.tpa_mst OWNER TO root;

--
-- TOC entry 261 (class 1259 OID 27882)
-- Name: tpa_mst_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.tpa_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.tpa_mst_id_seq OWNER TO root;

--
-- TOC entry 234 (class 1259 OID 27841)
-- Name: treatment_type_mst; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.treatment_type_mst (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE healspan.treatment_type_mst OWNER TO root;

--
-- TOC entry 262 (class 1259 OID 27883)
-- Name: treatment_type_mst_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.treatment_type_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.treatment_type_mst_id_seq OWNER TO root;

--
-- TOC entry 235 (class 1259 OID 27846)
-- Name: user_mst; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.user_mst (
    id bigint NOT NULL,
    email character varying(255),
    first_name character varying(255),
    is_active boolean,
    last_name character varying(255),
    middle_name character varying(255),
    mobile_no character varying(255),
    password character varying(255),
    username character varying(255),
    hospital_mst_id bigint,
    user_role_mst_id bigint
);


ALTER TABLE healspan.user_mst OWNER TO root;

--
-- TOC entry 263 (class 1259 OID 27884)
-- Name: user_mst_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.user_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.user_mst_id_seq OWNER TO root;

--
-- TOC entry 236 (class 1259 OID 27853)
-- Name: user_role_mst; Type: TABLE; Schema: healspan; Owner: root
--

CREATE TABLE healspan.user_role_mst (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE healspan.user_role_mst OWNER TO root;

--
-- TOC entry 264 (class 1259 OID 27885)
-- Name: user_role_mst_id_seq; Type: SEQUENCE; Schema: healspan; Owner: root
--

CREATE SEQUENCE healspan.user_role_mst_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE healspan.user_role_mst_id_seq OWNER TO root;

--
-- TOC entry 4495 (class 0 OID 27702)
-- Dependencies: 209
-- Data for Name: chronic_illness_mst; Type: TABLE DATA; Schema: healspan; Owner: root
--

INSERT INTO healspan.chronic_illness_mst VALUES (1, 'Acute Illness');
INSERT INTO healspan.chronic_illness_mst VALUES (2, 'Sub Acute Illnes');
INSERT INTO healspan.chronic_illness_mst VALUES (3, 'Acute Onset Chronic Illness');
INSERT INTO healspan.chronic_illness_mst VALUES (4, 'Chronic Illnesses');


--
-- TOC entry 4499 (class 0 OID 27724)
-- Dependencies: 213
-- Data for Name: claim_stage_mst; Type: TABLE DATA; Schema: healspan; Owner: root
--

INSERT INTO healspan.claim_stage_mst VALUES (1, 'Initial Authorization');
INSERT INTO healspan.claim_stage_mst VALUES (2, 'Enhancement');
INSERT INTO healspan.claim_stage_mst VALUES (3, 'Discharge');
INSERT INTO healspan.claim_stage_mst VALUES (4, 'Final Claim');


--
-- TOC entry 4551 (class 0 OID 28059)
-- Dependencies: 265
-- Data for Name: diagnosis_mst; Type: TABLE DATA; Schema: healspan; Owner: root
--

INSERT INTO healspan.diagnosis_mst VALUES (2, 'Varicose Veins');
INSERT INTO healspan.diagnosis_mst VALUES (1, 'Maternity');

--
-- TOC entry 4502 (class 0 OID 27741)
-- Dependencies: 216
-- Data for Name: document_type_mst; Type: TABLE DATA; Schema: healspan; Owner: root
--

INSERT INTO healspan.document_type_mst VALUES (1, 'Mandatory');
INSERT INTO healspan.document_type_mst VALUES (2, 'Rule Engine');
INSERT INTO healspan.document_type_mst VALUES (3, 'Reviewer');
INSERT INTO healspan.document_type_mst VALUES (4, 'Other');


--
-- TOC entry 4503 (class 0 OID 27746)
-- Dependencies: 217
-- Data for Name: gender_mst; Type: TABLE DATA; Schema: healspan; Owner: root
--

INSERT INTO healspan.gender_mst VALUES (1, 'Male');
INSERT INTO healspan.gender_mst VALUES (2, 'Female');
INSERT INTO healspan.gender_mst VALUES (3, 'Others');


--
-- TOC entry 4504 (class 0 OID 27751)
-- Dependencies: 218
-- Data for Name: hospital_mst; Type: TABLE DATA; Schema: healspan; Owner: root
--

INSERT INTO healspan.hospital_mst VALUES (1, 'Fortiz_123', 'Fortiz');
INSERT INTO healspan.hospital_mst VALUES (2, 'Jupiter_123', 'Jupiter');
INSERT INTO healspan.hospital_mst VALUES (3, 'Bethny_123', 'Bethny');
INSERT INTO healspan.hospital_mst VALUES (4, 'Lilavati_123', 'Lilavati');


--
-- TOC entry 4507 (class 0 OID 27770)
-- Dependencies: 221
-- Data for Name: mandatory_documents_mst; Type: TABLE DATA; Schema: healspan; Owner: root
--

INSERT INTO healspan.mandatory_documents_mst VALUES (1, 'Report supporting the diagnosis');
INSERT INTO healspan.mandatory_documents_mst VALUES (2, 'Etiology of ailment/Consultation papers');
INSERT INTO healspan.mandatory_documents_mst VALUES (3, 'Patient address proof (Pref. Aadhar)');
INSERT INTO healspan.mandatory_documents_mst VALUES (4, 'Insured PAN Card');
INSERT INTO healspan.mandatory_documents_mst VALUES (5, 'Claim Form (Part A)');
INSERT INTO healspan.mandatory_documents_mst VALUES (6, 'Report supporting the diagnosis');
INSERT INTO healspan.mandatory_documents_mst VALUES (8, 'Patient address proof (Pref. Aadhar)');
INSERT INTO healspan.mandatory_documents_mst VALUES (9, 'Insured PAN Card');
INSERT INTO healspan.mandatory_documents_mst VALUES (10, 'Claim Form (Part A)');
INSERT INTO healspan.mandatory_documents_mst VALUES (11, 'All the reports pertaining to the lab bills submitted');
INSERT INTO healspan.mandatory_documents_mst VALUES (12, 'All medicine prescription papers for the medicine bills submitted');
INSERT INTO healspan.mandatory_documents_mst VALUES (13, 'Detailed IP costwise itemwise break up bill');
INSERT INTO healspan.mandatory_documents_mst VALUES (14, 'Detailed Discharge Summary');
INSERT INTO healspan.mandatory_documents_mst VALUES (15, 'Patient PAN Card');
INSERT INTO healspan.mandatory_documents_mst VALUES (16, 'Patient address proof (Pref. Aadhar)');
INSERT INTO healspan.mandatory_documents_mst VALUES (17, 'Insurance card');
INSERT INTO healspan.mandatory_documents_mst VALUES (18, 'Final Bill');
INSERT INTO healspan.mandatory_documents_mst VALUES (19, 'Claim Form (Part A & B)');
INSERT INTO healspan.mandatory_documents_mst VALUES (20, 'Discharge Summary (with seal & signature)');
INSERT INTO healspan.mandatory_documents_mst VALUES (7, 'Consultation papers');


--
-- TOC entry 4510 (class 0 OID 27787)
-- Dependencies: 224
-- Data for Name: other_costs_mst; Type: TABLE DATA; Schema: healspan; Owner: root
--

INSERT INTO healspan.other_costs_mst VALUES (1, 'Medicines');
INSERT INTO healspan.other_costs_mst VALUES (2, 'Consumables');
INSERT INTO healspan.other_costs_mst VALUES (3, 'Consultation');
INSERT INTO healspan.other_costs_mst VALUES (4, 'OT Charges');
INSERT INTO healspan.other_costs_mst VALUES (5, 'Surgeon & Anasthesia');
INSERT INTO healspan.other_costs_mst VALUES (6, 'Investigations');
INSERT INTO healspan.other_costs_mst VALUES (7, 'Other hospital expenses');
INSERT INTO healspan.other_costs_mst VALUES (8, 'Package');


--
-- TOC entry 4513 (class 0 OID 27804)
-- Dependencies: 227
-- Data for Name: procedure_mst; Type: TABLE DATA; Schema: healspan; Owner: root
--

INSERT INTO healspan.procedure_mst VALUES (1, 'Full Term Normal Delivery');
INSERT INTO healspan.procedure_mst VALUES (2, 'Lower Segment Caesarean Section');
INSERT INTO healspan.procedure_mst VALUES (3, 'Dilatation and Curettage');
INSERT INTO healspan.procedure_mst VALUES (4, 'Salphingectomy');
INSERT INTO healspan.procedure_mst VALUES (5, 'Cervical Encerclage');
INSERT INTO healspan.procedure_mst VALUES (6, 'Gestational Diabetes');
INSERT INTO healspan.procedure_mst VALUES (7, 'Threatened Abortion/Heamorrhage In Early Pregnancy');
INSERT INTO healspan.procedure_mst VALUES (8, 'Laser Therapy');
INSERT INTO healspan.procedure_mst VALUES (9, 'Sclerotherapy');
INSERT INTO healspan.procedure_mst VALUES (10, 'Radiofrequency Ablation');
INSERT INTO healspan.procedure_mst VALUES (11, 'Ambulatory Phlebectomy');
INSERT INTO healspan.procedure_mst VALUES (12, 'Vein Stripping');
INSERT INTO healspan.procedure_mst VALUES (13, 'Avulsion of Perforator');


--
-- TOC entry 4515 (class 0 OID 27816)
-- Dependencies: 229
-- Data for Name: relationship_mst; Type: TABLE DATA; Schema: healspan; Owner: root
--

INSERT INTO healspan.relationship_mst VALUES (1, 'Self');
INSERT INTO healspan.relationship_mst VALUES (2, 'Spouse');
INSERT INTO healspan.relationship_mst VALUES (3, 'Father');
INSERT INTO healspan.relationship_mst VALUES (4, 'Mother');
INSERT INTO healspan.relationship_mst VALUES (5, 'Father in law');
INSERT INTO healspan.relationship_mst VALUES (6, 'Mother in law');
INSERT INTO healspan.relationship_mst VALUES (7, 'Son');
INSERT INTO healspan.relationship_mst VALUES (8, 'Daughter');
INSERT INTO healspan.relationship_mst VALUES (9, 'Sibling');
INSERT INTO healspan.relationship_mst VALUES (10, 'Other');


--
-- TOC entry 4516 (class 0 OID 27821)
-- Dependencies: 230
-- Data for Name: room_category_mst; Type: TABLE DATA; Schema: healspan; Owner: root
--

INSERT INTO healspan.room_category_mst VALUES (1, 'Single Private AC');
INSERT INTO healspan.room_category_mst VALUES (2, 'Delux Room');
INSERT INTO healspan.room_category_mst VALUES (3, 'Twin Sharing');
INSERT INTO healspan.room_category_mst VALUES (4, 'Triple Sharing');
INSERT INTO healspan.room_category_mst VALUES (5, 'General Ward');
INSERT INTO healspan.room_category_mst VALUES (6, 'Super Delux');
INSERT INTO healspan.room_category_mst VALUES (7, 'Suite');
INSERT INTO healspan.room_category_mst VALUES (8, 'Non/Ac room');
INSERT INTO healspan.room_category_mst VALUES (9, 'ICU/NICU/SICU');
INSERT INTO healspan.room_category_mst VALUES (10, 'Daycare');
INSERT INTO healspan.room_category_mst VALUES (11, 'OPD');


--
-- TOC entry 4517 (class 0 OID 27826)
-- Dependencies: 231
-- Data for Name: speciality_mst; Type: TABLE DATA; Schema: healspan; Owner: root
--

INSERT INTO healspan.speciality_mst VALUES (1, 'Neurology');
INSERT INTO healspan.speciality_mst VALUES (2, 'Cardiology');
INSERT INTO healspan.speciality_mst VALUES (3, 'CardioRespiratory');
INSERT INTO healspan.speciality_mst VALUES (4, 'Opthalmology');
INSERT INTO healspan.speciality_mst VALUES (5, 'Nephrology');
INSERT INTO healspan.speciality_mst VALUES (6, 'Endocrinology');
INSERT INTO healspan.speciality_mst VALUES (7, 'Muskuloskeletal');
INSERT INTO healspan.speciality_mst VALUES (8, 'Obs and Gynacecology');
INSERT INTO healspan.speciality_mst VALUES (9, 'General Medicine');
INSERT INTO healspan.speciality_mst VALUES (10, 'General Surgery');


--
-- TOC entry 4518 (class 0 OID 27831)
-- Dependencies: 232
-- Data for Name: status_mst; Type: TABLE DATA; Schema: healspan; Owner: root
--

INSERT INTO healspan.status_mst VALUES (1, 'Created');
INSERT INTO healspan.status_mst VALUES (2, 'Draft');
INSERT INTO healspan.status_mst VALUES (3, 'Submitted');
INSERT INTO healspan.status_mst VALUES (4, 'Reviewer Assigned');
INSERT INTO healspan.status_mst VALUES (5, 'Pending Review');
INSERT INTO healspan.status_mst VALUES (6, 'Reviewed');
INSERT INTO healspan.status_mst VALUES (7, 'Pending Documents');
INSERT INTO healspan.status_mst VALUES (8, 'Pending HS Approval');
INSERT INTO healspan.status_mst VALUES (9, 'Pending TPA Approval');
INSERT INTO healspan.status_mst VALUES (10, 'TPA Query Raised');
INSERT INTO healspan.status_mst VALUES (11, 'TPA Query Responded');
INSERT INTO healspan.status_mst VALUES (12, 'Rejected');
INSERT INTO healspan.status_mst VALUES (13, 'Hard copies to be sent');
INSERT INTO healspan.status_mst VALUES (14, 'Documents dispatched');
INSERT INTO healspan.status_mst VALUES (15, 'Approved');
INSERT INTO healspan.status_mst VALUES (16, 'Settled');
INSERT INTO healspan.status_mst VALUES (17, 'Rejected');


--
-- TOC entry 4519 (class 0 OID 27836)
-- Dependencies: 233
-- Data for Name: tpa_mst; Type: TABLE DATA; Schema: healspan; Owner: root
--

INSERT INTO healspan.tpa_mst VALUES (1, 'Medvantage Insurance TPA Private Limited (formerly known as United Health Care Parekh Insurance TPA Private Limited)');
INSERT INTO healspan.tpa_mst VALUES (2, 'Medi Assist Insurance TPA Private Limited');
INSERT INTO healspan.tpa_mst VALUES (3, 'MDIndia Health Insurance TPA Private Limited');
INSERT INTO healspan.tpa_mst VALUES (4, 'Paramount Health Services & Insurance TPA Private Limited');
INSERT INTO healspan.tpa_mst VALUES (5, 'Heritage Health Insurance TPA Private Limited');
INSERT INTO healspan.tpa_mst VALUES (6, 'Family Health Plan Insurance TPA Limited');
INSERT INTO healspan.tpa_mst VALUES (7, 'Raksha Health Insurance TPA Private Limited');
INSERT INTO healspan.tpa_mst VALUES (8, 'Vidal Health Insurance TPA Private Limited');
INSERT INTO healspan.tpa_mst VALUES (9, 'East West Assist Insurance TPA Private Limited');
INSERT INTO healspan.tpa_mst VALUES (10, 'Medsave Health Insurance TPA Limited');
INSERT INTO healspan.tpa_mst VALUES (11, 'Genins India Insurance TPA Limited');
INSERT INTO healspan.tpa_mst VALUES (12, 'Health India Insurance TPA Services Private Limited');
INSERT INTO healspan.tpa_mst VALUES (13, 'Good Health Insurance TPA Limited');
INSERT INTO healspan.tpa_mst VALUES (14, 'Park Mediclaim Insurance TPA Private Limited');
INSERT INTO healspan.tpa_mst VALUES (15, 'Safeway Insurance TPA Private Limited');
INSERT INTO healspan.tpa_mst VALUES (16, 'Anmol Medicare Insurance TPA Limited');
INSERT INTO healspan.tpa_mst VALUES (17, 'Rothshield Insurance TPA Limited');
INSERT INTO healspan.tpa_mst VALUES (18, 'Ericson Insurance TPA Private Limited');
INSERT INTO healspan.tpa_mst VALUES (19, 'Health Insurance TPA of India Limited');
INSERT INTO healspan.tpa_mst VALUES (20, 'Vision Digital Insurance TPA Private Limited');


--
-- TOC entry 4520 (class 0 OID 27841)
-- Dependencies: 234
-- Data for Name: treatment_type_mst; Type: TABLE DATA; Schema: healspan; Owner: root
--

INSERT INTO healspan.treatment_type_mst VALUES (1, 'Medical management');
INSERT INTO healspan.treatment_type_mst VALUES (2, 'Surgical management');


--
-- TOC entry 4521 (class 0 OID 27846)
-- Dependencies: 235
-- Data for Name: user_mst; Type: TABLE DATA; Schema: healspan; Owner: root
--

INSERT INTO healspan.user_mst VALUES (1, NULL, 'System', true, NULL, NULL, NULL, 'System', 'System', NULL, 1);
INSERT INTO healspan.user_mst VALUES (2, NULL, 'Hospital-1', true, NULL, NULL, NULL, 'Pass@123', 'Hospital-1', 1, 2);
INSERT INTO healspan.user_mst VALUES (3, NULL, 'Hospital-2', true, NULL, NULL, NULL, 'Pass@123', 'Hospital-2', 2, 2);
INSERT INTO healspan.user_mst VALUES (4, NULL, 'Hospital-3', true, NULL, NULL, NULL, 'Pass@123', 'Hospital-3', 3, 2);
INSERT INTO healspan.user_mst VALUES (5, NULL, 'Reviewer-1', true, NULL, NULL, NULL, 'Pass@123', 'Reviewer-1', NULL, 3);
INSERT INTO healspan.user_mst VALUES (6, NULL, 'Reviewer-2', true, NULL, NULL, NULL, 'Pass@123', 'Reviewer-2', NULL, 3);
INSERT INTO healspan.user_mst VALUES (7, NULL, 'Reviewer-3', true, NULL, NULL, NULL, 'Pass@123', 'Reviewer-3', NULL, 3);


--
-- TOC entry 4522 (class 0 OID 27853)
-- Dependencies: 236
-- Data for Name: user_role_mst; Type: TABLE DATA; Schema: healspan; Owner: root
--

INSERT INTO healspan.user_role_mst VALUES (1, 'Admin');
INSERT INTO healspan.user_role_mst VALUES (2, 'Hospital');
INSERT INTO healspan.user_role_mst VALUES (3, 'Healspan');
INSERT INTO healspan.user_role_mst VALUES (4, 'Courier');


--
-- TOC entry 4557 (class 0 OID 0)
-- Dependencies: 237
-- Name: chronic_illness_mst_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.chronic_illness_mst_id_seq', 1, false);


--
-- TOC entry 4558 (class 0 OID 0)
-- Dependencies: 238
-- Name: claim_assignment_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.claim_assignment_id_seq', 13, true);


--
-- TOC entry 4559 (class 0 OID 0)
-- Dependencies: 239
-- Name: claim_info_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.claim_info_id_seq', 39, true);


--
-- TOC entry 4560 (class 0 OID 0)
-- Dependencies: 240
-- Name: claim_stage_link_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.claim_stage_link_id_seq', 44, true);


--
-- TOC entry 4561 (class 0 OID 0)
-- Dependencies: 241
-- Name: claim_stage_mst_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.claim_stage_mst_id_seq', 1, false);


--
-- TOC entry 4562 (class 0 OID 0)
-- Dependencies: 242
-- Name: diagnosis_mst_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.diagnosis_mst_id_seq', 1, false);


--
-- TOC entry 4563 (class 0 OID 0)
-- Dependencies: 243
-- Name: document_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.document_id_seq', 102, true);


--
-- TOC entry 4564 (class 0 OID 0)
-- Dependencies: 244
-- Name: document_type_mst_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.document_type_mst_id_seq', 1, false);


--
-- TOC entry 4565 (class 0 OID 0)
-- Dependencies: 245
-- Name: gender_mst_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.gender_mst_id_seq', 1, false);


--
-- TOC entry 4566 (class 0 OID 0)
-- Dependencies: 246
-- Name: hospital_mst_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.hospital_mst_id_seq', 1, false);


--
-- TOC entry 4567 (class 0 OID 0)
-- Dependencies: 247
-- Name: insurance_company_mst_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.insurance_company_mst_id_seq', 1, false);


--
-- TOC entry 4568 (class 0 OID 0)
-- Dependencies: 248
-- Name: insurance_info_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.insurance_info_id_seq', 25, true);


--
-- TOC entry 4569 (class 0 OID 0)
-- Dependencies: 249
-- Name: mandatory_documents_mst_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.mandatory_documents_mst_id_seq', 1, false);


--
-- TOC entry 4570 (class 0 OID 0)
-- Dependencies: 250
-- Name: medical_chronic_illness_link_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.medical_chronic_illness_link_id_seq', 51, true);


--
-- TOC entry 4571 (class 0 OID 0)
-- Dependencies: 251
-- Name: medical_info_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.medical_info_id_seq', 36, true);


--
-- TOC entry 4572 (class 0 OID 0)
-- Dependencies: 252
-- Name: other_costs_mst_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.other_costs_mst_id_seq', 1, false);


--
-- TOC entry 4573 (class 0 OID 0)
-- Dependencies: 253
-- Name: patient_info_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.patient_info_id_seq', 96, true);


--
-- TOC entry 4574 (class 0 OID 0)
-- Dependencies: 254
-- Name: patient_othercost_link_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.patient_othercost_link_id_seq', 133, true);


--
-- TOC entry 4575 (class 0 OID 0)
-- Dependencies: 255
-- Name: procedure_mst_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.procedure_mst_id_seq', 1, false);


--
-- TOC entry 4576 (class 0 OID 0)
-- Dependencies: 256
-- Name: question_answer_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.question_answer_id_seq', 6, true);


--
-- TOC entry 4577 (class 0 OID 0)
-- Dependencies: 257
-- Name: relationship_mst_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.relationship_mst_id_seq', 1, false);


--
-- TOC entry 4578 (class 0 OID 0)
-- Dependencies: 258
-- Name: room_category_mst_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.room_category_mst_id_seq', 1, false);


--
-- TOC entry 4579 (class 0 OID 0)
-- Dependencies: 259
-- Name: speciality_mst_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.speciality_mst_id_seq', 1, false);


--
-- TOC entry 4580 (class 0 OID 0)
-- Dependencies: 260
-- Name: status_mst_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.status_mst_id_seq', 1, false);


--
-- TOC entry 4581 (class 0 OID 0)
-- Dependencies: 261
-- Name: tpa_mst_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.tpa_mst_id_seq', 1, false);


--
-- TOC entry 4582 (class 0 OID 0)
-- Dependencies: 262
-- Name: treatment_type_mst_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.treatment_type_mst_id_seq', 1, false);


--
-- TOC entry 4583 (class 0 OID 0)
-- Dependencies: 263
-- Name: user_mst_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.user_mst_id_seq', 1, false);


--
-- TOC entry 4584 (class 0 OID 0)
-- Dependencies: 264
-- Name: user_role_mst_id_seq; Type: SEQUENCE SET; Schema: healspan; Owner: root
--

SELECT pg_catalog.setval('healspan.user_role_mst_id_seq', 1, false);


--
-- TOC entry 4267 (class 2606 OID 27706)
-- Name: chronic_illness_mst chronic_illness_mst_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.chronic_illness_mst
    ADD CONSTRAINT chronic_illness_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 4269 (class 2606 OID 27713)
-- Name: claim_assignment claim_assignment_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.claim_assignment
    ADD CONSTRAINT claim_assignment_pkey PRIMARY KEY (id);


--
-- TOC entry 4271 (class 2606 OID 27718)
-- Name: claim_info claim_info_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.claim_info
    ADD CONSTRAINT claim_info_pkey PRIMARY KEY (id);


--
-- TOC entry 4273 (class 2606 OID 27723)
-- Name: claim_stage_link claim_stage_link_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.claim_stage_link
    ADD CONSTRAINT claim_stage_link_pkey PRIMARY KEY (id);


--
-- TOC entry 4275 (class 2606 OID 27728)
-- Name: claim_stage_mst claim_stage_mst_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.claim_stage_mst
    ADD CONSTRAINT claim_stage_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 4323 (class 2606 OID 28063)
-- Name: diagnosis_mst diagnosis_mst_new_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.diagnosis_mst
    ADD CONSTRAINT diagnosis_mst_new_pkey PRIMARY KEY (id);


--
-- TOC entry 4277 (class 2606 OID 27733)
-- Name: diagnosis_mst_old diagnosis_mst_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.diagnosis_mst_old
    ADD CONSTRAINT diagnosis_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 4279 (class 2606 OID 27740)
-- Name: document document_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.document
    ADD CONSTRAINT document_pkey PRIMARY KEY (id);


--
-- TOC entry 4281 (class 2606 OID 27745)
-- Name: document_type_mst document_type_mst_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.document_type_mst
    ADD CONSTRAINT document_type_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 4283 (class 2606 OID 27750)
-- Name: gender_mst gender_mst_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.gender_mst
    ADD CONSTRAINT gender_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 4285 (class 2606 OID 27757)
-- Name: hospital_mst hospital_mst_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.hospital_mst
    ADD CONSTRAINT hospital_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 4287 (class 2606 OID 27762)
-- Name: insurance_company_mst insurance_company_mst_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.insurance_company_mst
    ADD CONSTRAINT insurance_company_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 4289 (class 2606 OID 27769)
-- Name: insurance_info insurance_info_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.insurance_info
    ADD CONSTRAINT insurance_info_pkey PRIMARY KEY (id);


--
-- TOC entry 4291 (class 2606 OID 27774)
-- Name: mandatory_documents_mst mandatory_documents_mst_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.mandatory_documents_mst
    ADD CONSTRAINT mandatory_documents_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 4293 (class 2606 OID 27779)
-- Name: medical_chronic_illness_link medical_chronic_illness_link_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.medical_chronic_illness_link
    ADD CONSTRAINT medical_chronic_illness_link_pkey PRIMARY KEY (id);


--
-- TOC entry 4295 (class 2606 OID 27786)
-- Name: medical_info medical_info_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.medical_info
    ADD CONSTRAINT medical_info_pkey PRIMARY KEY (id);


--
-- TOC entry 4297 (class 2606 OID 27791)
-- Name: other_costs_mst other_costs_mst_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.other_costs_mst
    ADD CONSTRAINT other_costs_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 4299 (class 2606 OID 27798)
-- Name: patient_info patient_info_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.patient_info
    ADD CONSTRAINT patient_info_pkey PRIMARY KEY (id);


--
-- TOC entry 4301 (class 2606 OID 27803)
-- Name: patient_othercost_link patient_othercost_link_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.patient_othercost_link
    ADD CONSTRAINT patient_othercost_link_pkey PRIMARY KEY (id);


--
-- TOC entry 4303 (class 2606 OID 27808)
-- Name: procedure_mst procedure_mst_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.procedure_mst
    ADD CONSTRAINT procedure_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 4305 (class 2606 OID 27815)
-- Name: question_answer question_answer_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.question_answer
    ADD CONSTRAINT question_answer_pkey PRIMARY KEY (id);


--
-- TOC entry 4307 (class 2606 OID 27820)
-- Name: relationship_mst relationship_mst_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.relationship_mst
    ADD CONSTRAINT relationship_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 4309 (class 2606 OID 27825)
-- Name: room_category_mst room_category_mst_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.room_category_mst
    ADD CONSTRAINT room_category_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 4311 (class 2606 OID 27830)
-- Name: speciality_mst speciality_mst_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.speciality_mst
    ADD CONSTRAINT speciality_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 4313 (class 2606 OID 27835)
-- Name: status_mst status_mst_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.status_mst
    ADD CONSTRAINT status_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 4315 (class 2606 OID 27840)
-- Name: tpa_mst tpa_mst_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.tpa_mst
    ADD CONSTRAINT tpa_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 4317 (class 2606 OID 27845)
-- Name: treatment_type_mst treatment_type_mst_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.treatment_type_mst
    ADD CONSTRAINT treatment_type_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 4319 (class 2606 OID 27852)
-- Name: user_mst user_mst_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.user_mst
    ADD CONSTRAINT user_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 4321 (class 2606 OID 27857)
-- Name: user_role_mst user_role_mst_pkey; Type: CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.user_role_mst
    ADD CONSTRAINT user_role_mst_pkey PRIMARY KEY (id);


--
-- TOC entry 4344 (class 2606 OID 27986)
-- Name: medical_info fk1reelpd9u4dj4xdc7fsf1mo5l; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.medical_info
    ADD CONSTRAINT fk1reelpd9u4dj4xdc7fsf1mo5l FOREIGN KEY (speciality_mst_id) REFERENCES healspan.speciality_mst(id);


--
-- TOC entry 4347 (class 2606 OID 28001)
-- Name: patient_info fk31xa9k3yh4uo40ri9oj4yom8q; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.patient_info
    ADD CONSTRAINT fk31xa9k3yh4uo40ri9oj4yom8q FOREIGN KEY (hospital_mst_id) REFERENCES healspan.hospital_mst(id);


--
-- TOC entry 4332 (class 2606 OID 27926)
-- Name: claim_stage_link fk4a3bolyyfie6cbs5yyo6mfgau; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.claim_stage_link
    ADD CONSTRAINT fk4a3bolyyfie6cbs5yyo6mfgau FOREIGN KEY (patient_info_id) REFERENCES healspan.patient_info(id);


--
-- TOC entry 4340 (class 2606 OID 27966)
-- Name: medical_chronic_illness_link fk5xupi3ts9korv9ynmokml89qn; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.medical_chronic_illness_link
    ADD CONSTRAINT fk5xupi3ts9korv9ynmokml89qn FOREIGN KEY (chronic_illness_mst_id) REFERENCES healspan.chronic_illness_mst(id);


--
-- TOC entry 4349 (class 2606 OID 28011)
-- Name: patient_info fk6tab0bsx4qwk4f4boxcsgv1gn; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.patient_info
    ADD CONSTRAINT fk6tab0bsx4qwk4f4boxcsgv1gn FOREIGN KEY (procedure_mst_id) REFERENCES healspan.procedure_mst(id);


--
-- TOC entry 4346 (class 2606 OID 27996)
-- Name: patient_info fk78prxrdgisdhxpx6sr4c0g1jp; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.patient_info
    ADD CONSTRAINT fk78prxrdgisdhxpx6sr4c0g1jp FOREIGN KEY (gender_mst_id) REFERENCES healspan.gender_mst(id);


--
-- TOC entry 4326 (class 2606 OID 27896)
-- Name: claim_info fk7p9on31ek2wccogv142huqsc9; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.claim_info
    ADD CONSTRAINT fk7p9on31ek2wccogv142huqsc9 FOREIGN KEY (hospital_mst_id) REFERENCES healspan.hospital_mst(id);


--
-- TOC entry 4352 (class 2606 OID 28026)
-- Name: patient_othercost_link fk7umk4i64ivg1ojh6ljs2w4drd; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.patient_othercost_link
    ADD CONSTRAINT fk7umk4i64ivg1ojh6ljs2w4drd FOREIGN KEY (patient_info_id) REFERENCES healspan.patient_info(id);


--
-- TOC entry 4330 (class 2606 OID 27916)
-- Name: claim_stage_link fk82tvqym5yvba5mxk41pk3a4i4; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.claim_stage_link
    ADD CONSTRAINT fk82tvqym5yvba5mxk41pk3a4i4 FOREIGN KEY (insurance_info_id) REFERENCES healspan.insurance_info(id);


--
-- TOC entry 4337 (class 2606 OID 27951)
-- Name: insurance_info fk8d3hqm78vbdmvx1q3mh99hdh8; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.insurance_info
    ADD CONSTRAINT fk8d3hqm78vbdmvx1q3mh99hdh8 FOREIGN KEY (insurance_company_mst_id) REFERENCES healspan.insurance_company_mst(id);


--
-- TOC entry 4336 (class 2606 OID 27946)
-- Name: document fk8dpi6l0bqy0wlltiwgy46bfxy; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.document
    ADD CONSTRAINT fk8dpi6l0bqy0wlltiwgy46bfxy FOREIGN KEY (medical_info_id) REFERENCES healspan.medical_info(id);


--
-- TOC entry 4339 (class 2606 OID 27961)
-- Name: insurance_info fkagmv1479jq8x9kb1c4r7uc4xw; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.insurance_info
    ADD CONSTRAINT fkagmv1479jq8x9kb1c4r7uc4xw FOREIGN KEY (tpa_mst_id) REFERENCES healspan.tpa_mst(id);


--
-- TOC entry 4351 (class 2606 OID 28021)
-- Name: patient_othercost_link fkbvmbo5ik7vg7vmsep25rp75om; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.patient_othercost_link
    ADD CONSTRAINT fkbvmbo5ik7vg7vmsep25rp75om FOREIGN KEY (other_costs_mst_id) REFERENCES healspan.other_costs_mst(id);


--
-- TOC entry 4342 (class 2606 OID 27976)
-- Name: medical_info fkceylqmb8sjgok34sg81fajxra; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.medical_info
    ADD CONSTRAINT fkceylqmb8sjgok34sg81fajxra FOREIGN KEY (diagnosis_mst_id) REFERENCES healspan.diagnosis_mst_old(id);


--
-- TOC entry 4355 (class 2606 OID 28041)
-- Name: user_mst fke49u0iellvdc4r5idvllyuhsm; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.user_mst
    ADD CONSTRAINT fke49u0iellvdc4r5idvllyuhsm FOREIGN KEY (user_role_mst_id) REFERENCES healspan.user_role_mst(id);


--
-- TOC entry 4331 (class 2606 OID 27921)
-- Name: claim_stage_link fkea5i40j67jpgrpd7qh3hxlgar; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.claim_stage_link
    ADD CONSTRAINT fkea5i40j67jpgrpd7qh3hxlgar FOREIGN KEY (medical_info_id) REFERENCES healspan.medical_info(id);


--
-- TOC entry 4335 (class 2606 OID 27941)
-- Name: document fkf0jxcsfl9mvbtg5nuvcgmn5w; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.document
    ADD CONSTRAINT fkf0jxcsfl9mvbtg5nuvcgmn5w FOREIGN KEY (document_type_mst_id) REFERENCES healspan.document_type_mst(id);


--
-- TOC entry 4354 (class 2606 OID 28036)
-- Name: user_mst fkf1nbgg4qrycwgt4rubi49yi75; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.user_mst
    ADD CONSTRAINT fkf1nbgg4qrycwgt4rubi49yi75 FOREIGN KEY (hospital_mst_id) REFERENCES healspan.hospital_mst(id);


--
-- TOC entry 4343 (class 2606 OID 27981)
-- Name: medical_info fkg7lod6ygw0cq36p1viypie3y7; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.medical_info
    ADD CONSTRAINT fkg7lod6ygw0cq36p1viypie3y7 FOREIGN KEY (procedure_mst_id) REFERENCES healspan.procedure_mst(id);


--
-- TOC entry 4341 (class 2606 OID 27971)
-- Name: medical_chronic_illness_link fkhoxc5bo3sf5fxi5tq2g6omeo0; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.medical_chronic_illness_link
    ADD CONSTRAINT fkhoxc5bo3sf5fxi5tq2g6omeo0 FOREIGN KEY (medical_info_id) REFERENCES healspan.medical_info(id);


--
-- TOC entry 4348 (class 2606 OID 28006)
-- Name: patient_info fkik32reu096ibwglgfpohublcp; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.patient_info
    ADD CONSTRAINT fkik32reu096ibwglgfpohublcp FOREIGN KEY (other_costs_mst_id) REFERENCES healspan.other_costs_mst(id);


--
-- TOC entry 4325 (class 2606 OID 27891)
-- Name: claim_assignment fkil7qkbivrsqt1jgt0tdh2srum; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.claim_assignment
    ADD CONSTRAINT fkil7qkbivrsqt1jgt0tdh2srum FOREIGN KEY (user_mst_id) REFERENCES healspan.user_mst(id);


--
-- TOC entry 4350 (class 2606 OID 28016)
-- Name: patient_info fkjw2ctletawjhidq8kvs14giru; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.patient_info
    ADD CONSTRAINT fkjw2ctletawjhidq8kvs14giru FOREIGN KEY (room_category_mst_id) REFERENCES healspan.room_category_mst(id);


--
-- TOC entry 4345 (class 2606 OID 27991)
-- Name: medical_info fkjyf88swsgxv0lnja0kiopg1j2; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.medical_info
    ADD CONSTRAINT fkjyf88swsgxv0lnja0kiopg1j2 FOREIGN KEY (treatment_type_mst_id) REFERENCES healspan.treatment_type_mst(id);


--
-- TOC entry 4333 (class 2606 OID 27931)
-- Name: claim_stage_link fkk83ncxh639txscbg8hu4bkg21; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.claim_stage_link
    ADD CONSTRAINT fkk83ncxh639txscbg8hu4bkg21 FOREIGN KEY (status_mst_id) REFERENCES healspan.status_mst(id);


--
-- TOC entry 4338 (class 2606 OID 27956)
-- Name: insurance_info fklexjnv8rg6xngmypr8nqccu5b; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.insurance_info
    ADD CONSTRAINT fklexjnv8rg6xngmypr8nqccu5b FOREIGN KEY (relationship_mst_id) REFERENCES healspan.relationship_mst(id);


--
-- TOC entry 4353 (class 2606 OID 28031)
-- Name: question_answer fklqa8vjacrwlkgoi7j0h3w46ub; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.question_answer
    ADD CONSTRAINT fklqa8vjacrwlkgoi7j0h3w46ub FOREIGN KEY (medical_info_id) REFERENCES healspan.medical_info(id);


--
-- TOC entry 4324 (class 2606 OID 27886)
-- Name: claim_assignment fknlgpoctjsshr7ghasc0hrglt4; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.claim_assignment
    ADD CONSTRAINT fknlgpoctjsshr7ghasc0hrglt4 FOREIGN KEY (claim_stage_link_id) REFERENCES healspan.claim_stage_link(id);


--
-- TOC entry 4334 (class 2606 OID 27936)
-- Name: claim_stage_link fknvi2ifr0f74gpwsx4ro6r0emm; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.claim_stage_link
    ADD CONSTRAINT fknvi2ifr0f74gpwsx4ro6r0emm FOREIGN KEY (user_mst_id) REFERENCES healspan.user_mst(id);


--
-- TOC entry 4327 (class 2606 OID 27901)
-- Name: claim_info fkonb8etyoi65haifwysk4fqsc8; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.claim_info
    ADD CONSTRAINT fkonb8etyoi65haifwysk4fqsc8 FOREIGN KEY (user_mst_id) REFERENCES healspan.user_mst(id);


--
-- TOC entry 4328 (class 2606 OID 27906)
-- Name: claim_stage_link fkrjryrvgimwg1h5ww78me4r3o6; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.claim_stage_link
    ADD CONSTRAINT fkrjryrvgimwg1h5ww78me4r3o6 FOREIGN KEY (claim_info_id) REFERENCES healspan.claim_info(id);


--
-- TOC entry 4329 (class 2606 OID 27911)
-- Name: claim_stage_link fkt7k2qryj5kp6s9uhswq94rap2; Type: FK CONSTRAINT; Schema: healspan; Owner: root
--

ALTER TABLE ONLY healspan.claim_stage_link
    ADD CONSTRAINT fkt7k2qryj5kp6s9uhswq94rap2 FOREIGN KEY (claim_stage_mst_id) REFERENCES healspan.claim_stage_mst(id);


-- Completed on 2022-12-29 15:31:03

--
-- PostgreSQL database dump complete
--

