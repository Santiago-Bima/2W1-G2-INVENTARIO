insert into zonas_almacenamiento (nombre_zona) values ('Corralito');
insert into zonas_almacenamiento (nombre_zona) values ('Almacén 1');
insert into zonas_almacenamiento (nombre_zona) values ('Almacén 2');

-- Remitos
insert into remitos (fecha_llegada, nro_remito, nro_orden_compra, rechazado, observaciones, nombre_proveedor)
values ('2023-11-14', '1', '1', false, 'coincide con la orden de compra', 'nombre 1');
insert into remitos (fecha_llegada, nro_remito, nro_orden_compra, rechazado, observaciones, nombre_proveedor)
values ('2022-10-30', '2', '3', false, 'coincide con la orden de compra', 'nombre 1');
insert into remitos (fecha_llegada, nro_remito, nro_orden_compra, rechazado, observaciones, nombre_proveedor)
values ('2022-10-30', '1', '1', true, 'coincide con la orden de compra', 'nombre 2');

insert into detalles_remito (id_remito, cantidad, nombre_producto, detalle)
values (1, 20, 'tornillo', 'tornillo');
insert into detalles_remito (id_remito, cantidad, nombre_producto, detalle)
values (1, 20, 'tornillo grande', 'tornillo grande');
insert into detalles_remito (id_remito, cantidad, nombre_producto, detalle)
values (2, 30, 'clavo grande', 'clavo grande');
insert into detalles_remito (id_remito, cantidad, nombre_producto, detalle)
values (3, 50, 'clavo mediano', 'clavo mediano');
insert into detalles_remito (id_remito, cantidad, nombre_producto, detalle)
values (3, 50, 'clavo pequeño', 'clavo pequeño');