INSERT INTO public.parametro_general (id_parametro_general, codigo) VALUES (50, 'DATOS_GENERADOS');

INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (10,true,'PERMISOS_CREATE','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (11,true,'PERMISOS_DELETE','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (12,true,'PERMISOS_READ','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (13,true,'PERMISOS_MODIFY','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (14,true,'PERFIL_CREATE','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (15,true,'PERFIL_DELETE','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (16,true,'PERFIL_READ','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (17,true,'PERFIL_MODIFY','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (18,true,'USUARIO_CREATE','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (19,true,'USUARIO_DELETE','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (20,true,'USUARIO_READ','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (21,true,'USUARIO_MODIFY','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (22,true,'PARAMETROS_CREATE','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (23,true,'PARAMETROS_DELETE','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (24,true,'PARAMETROS_READ','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (25,true,'PARAMETROS_MODIFY','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (30,true,'LISTADO_CLIENTE_CREATE','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (31,true,'LISTADO_CLIENTE_DELETE','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (32,true,'LISTADO_CLIENTE_READ','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (33,true,'LISTADO_CLIENTE_MODIFY','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (34,true,'MENSAJES_RECIBIDOS_CREATE','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (35,true,'MENSAJES_RECIBIDOS_DELETE','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (36,true,'MENSAJES_RECIBIDOS_READ','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (37,true,'MENSAJES_RECIBIDOS_MODIFY','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (38,true,'FAQ_CREATE','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (39,true,'FAQ_DELETE','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (40,true,'FAQ_READ','');
INSERT INTO public.seg_permiso (id_seg_permiso, activo, codigo, descripcion) VALUES (41,true,'FAQ_MODIFY','');

INSERT INTO public.seg_rol (id_seg_rol, codigo, nombre, super_rol) VALUES (2, 'ADM', 'Administrador', true);

INSERT INTO public.seg_perfil (id_seg_perfil, activo, nombre, rol_id_seg_rol) VALUES (1, true, 'Administrador', 2);

INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 10);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 11);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 12);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 13);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 14);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 15);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 16);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 17);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 18);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 19);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 20);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 21);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 22);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 23);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 24);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 25);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 30);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 31);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 32);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 33);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 34);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 35);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 36);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 37);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 38);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 39);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 40);
INSERT INTO public.seg_perfil_permiso (id_seg_perfil, id_seg_permiso) VALUES (1, 41);




-- Usuarios -- password encriptada=beto
INSERT INTO public.seg_usuario (id_seg_usuario, apellido, bloqueado, email, fecha_alta_password, intentos_fallidos_login, interno, nombre, password, requiere_cambio_password, nombre_usuario) VALUES (1, 'Gonzalez', false, 'avacotest@gmail.com', NULL, 0, true, 'Alberto', '$2a$10$9EmXkvYDoxA/rwi2.uxOue/I2Z2.6fzIubnhKnjFEQvCESbfzuqZm', false, 'adminbeto');
INSERT INTO public.seg_usuario (id_seg_usuario, apellido, bloqueado, email, fecha_alta_password, intentos_fallidos_login, interno, nombre, password, requiere_cambio_password, nombre_usuario) VALUES (2, 'Cabás', false, 'avacotest@gmail.com', '2018-06-06 02:54:21.392', 0, false, 'Victor', '$2a$10$9EmXkvYDoxA/rwi2.uxOue/I2Z2.6fzIubnhKnjFEQvCESbfzuqZm', false, 'adminvictor');

INSERT INTO public.seg_acceso (id_seg_acceso, perfil_id_seg_perfil, usuario_id_seg_usuario) VALUES (1, 1, 1);
INSERT INTO public.seg_acceso (id_seg_acceso, perfil_id_seg_perfil, usuario_id_seg_usuario) VALUES (2, 1, 2);

