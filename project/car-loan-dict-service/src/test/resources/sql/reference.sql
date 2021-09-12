insert into ref_refgroup(refgroupid, parentgroupid, groupname, groupsysname, description) values
(1, 0, 'ref group parent name 1', 'refgroupparentsysname1', 'description 1'),
(2, 1, 'ref group child name 2', 'refgroupchildsysname1', 'description 2'),
(3, 1, 'ref group child name 3', 'refgroupchildsysname2', 'description 3');

insert into ref_reference(referenceid, refgroupid, referencename, refsysname, description) values
(1, 2, 'ref name 1', 'refsysname1', 'description 1'),
(2, 2, 'ref name 2', 'refsysname2', 'description 2'),
(3, 2, 'ref name 3', 'refsysname3', 'description 3'),
(4, 2, 'ref name 4', 'refsysname4', 'description 4'),
(5, 2, 'ref name 5', 'refsysname5', 'description 5'),
(6, 2, 'ref name 6', 'refsysname6', 'description 6'),
(7, 2, 'ref name 7', 'refsysname7', 'description 7'),
(8, 2, 'ref name 8', 'refsysname8', 'description 8'),
(9, 3, 'ref name 9', 'refsysname9', 'description 9'),
(10, 3, 'ref name 10', 'refsysname10', 'description 10'),
(11, 3, 'ref name 11', 'refsysname11', 'description 11'),
(12, 3, 'ref name 12', 'refsysname12', 'description 12'),
(13, 3, 'ref name 13', 'refsysname13', 'description 13'),
(14, 3, 'ref name 14', 'refsysname14', 'description 14'),
(15, 3, 'ref name 15', 'refsysname15', 'description 15');