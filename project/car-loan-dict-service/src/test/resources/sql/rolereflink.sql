insert into ref_userrole(roleid, rolename, rolesysname, description) values
(1, 'role name 1', 'rolesysname1', 'description 1'),
(2, 'role name 2', 'rolesysname2', 'description 2'),
(3, 'role name 3', 'rolesysname3', 'description 3'),
(4, 'role name 4', 'rolesysname4', 'description 4'),
(5, 'role name 5', 'rolesysname5', 'description 5'),
(6, 'role name 6', 'rolesysname6', 'description 6'),
(7, 'role name 7', 'rolesysname7', 'description 7'),
(8, 'role name 8', 'rolesysname8', 'description 8'),
(9, 'role name 9', 'rolesysname9', 'description 9'),
(10, 'role name 10', 'rolesysname10', 'description 10'),
(11, 'role name 11', 'rolesysname11', 'description 11'),
(12, 'role name 12', 'rolesysname12', 'description 12'),
(13, 'role name 13', 'rolesysname13', 'description 13'),
(14, 'role name 14', 'rolesysname14', 'description 14'),
(15, 'role name 15', 'rolesysname15', 'description 15');

insert into ref_refgroup(refgroupid, parentgroupid, groupname, groupsysname, description) values
(1, 0, 'ref group parent name 1', 'refgroupparentsysname1', 'description 1');

insert into ref_reference(referenceid, refgroupid, referencename, refsysname, description) values
(1, 1, 'ref name 1', 'refsysname1', 'description 1'),
(2, 1, 'ref name 2', 'refsysname2', 'description 2'),
(3, 1, 'ref name 3', 'refsysname3', 'description 3'),
(4, 1, 'ref name 4', 'refsysname4', 'description 4'),
(5, 1, 'ref name 5', 'refsysname5', 'description 5'),
(6, 1, 'ref name 6', 'refsysname6', 'description 6'),
(7, 1, 'ref name 7', 'refsysname7', 'description 7'),
(8, 1, 'ref name 8', 'refsysname8', 'description 8'),
(9, 1, 'ref name 9', 'refsysname9', 'description 9'),
(10, 1, 'ref name 10', 'refsysname10', 'description 10'),
(11, 1, 'ref name 11', 'refsysname11', 'description 11'),
(12, 1, 'ref name 12', 'refsysname12', 'description 12'),
(13, 1, 'ref name 13', 'refsysname13', 'description 13'),
(14, 1, 'ref name 14', 'refsysname14', 'description 14'),
(15, 1, 'ref name 15', 'refsysname15', 'description 15');

insert into ref_rolereflink(linkid, referenceid, roleid) values
(1, 1, 1),
(2, 2, 2),
(3, 3, 3),
(4, 4, 4),
(5, 5, 5),
(6, 6, 6),
(7, 7, 7),
(8, 8, 8),
(9, 9, 9),
(10, 10, 10),
(11, 11, 11),
(12, 12, 12),
(13, 13, 13),
(14, 14, 14),
(15, 15, 15);