insert into zonas_almacenamiento (nombre_zona) values ('Corralito') ON CONFLICT (nombre_zona) DO NOTHING;
insert into zonas_almacenamiento (nombre_zona) values ('Almacén 1') ON CONFLICT (nombre_zona) DO NOTHING;
insert into zonas_almacenamiento (nombre_zona) values ('Almacén 2') ON CONFLICT (nombre_zona) DO NOTHING;
