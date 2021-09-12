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
(1, 1, 'ref name 1', 'refsysname1', 'description 1');

insert into ref_refitem(refitemid, referenceid, code, name, brief, description) values
(1, 1, 'code1', 'name1', 'brief1', 'description 1'),
(2, 1, 'code2', 'name2', 'brief2', 'description 2'),
(3, 1, 'code3', 'name3', 'brief3', 'description 3'),
(4, 1, 'code4', 'name4', 'brief4', 'description 4'),
(5, 1, 'code5', 'name5', 'brief5', 'description 5'),
(6, 1, 'code6', 'name6', 'brief6', 'description 6'),
(7, 1, 'code7', 'name7', 'brief7', 'description 7'),
(8, 1, 'code8', 'name8', 'brief8', 'description 8'),
(9, 1, 'code9', 'name9', 'brief9', 'description 9'),
(10, 1, 'code10', 'name10', 'brief10', 'description 10'),
(11, 1, 'code11', 'name11', 'brief11', 'description 11'),
(12, 1, 'code12', 'name12', 'brief12', 'description 12'),
(13, 1, 'code13', 'name13', 'brief13', 'description 13'),
(14, 1, 'code14', 'name14', 'brief14', 'description 14'),
(15, 1, 'code15', 'name15', 'brief15', 'description 15');

insert into ref_rolerefitemlink(linkid, refitemid, roleid) values
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

