ALTER TABLE IF EXISTS public.seg_usuario
    ADD COLUMN admin boolean;
	
update seg_usuario set admin = true;