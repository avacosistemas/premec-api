INSERT INTO public.parameter(id_parameter, description, key, value) VALUES (1,'descripcion 1','key_parametro 1','value_parametro 1');
INSERT INTO public.parameter(id_parameter, description, key, value) VALUES (2,'descripcion 2','key_parametro 2','value_parametro 2');
INSERT INTO public.parameter(id_parameter, description, key, value) VALUES (3,'descripcion 3','key_parametro 3','value_parametro 3');
INSERT INTO public.parameter(id_parameter, description, key, value) VALUES (4,'descripcion 4','key_parametro 4','value_parametro 4');

INSERT INTO public.i18n (id_i18n, description, lang) VALUES (1, 'Español', 'ES');

INSERT INTO public.word(id_word, key, value, i18n_id_i18n) VALUES (1, 'DISCLAIMER', 'DISCLAIMER ETC ETC', 1);