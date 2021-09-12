insert into ref_refgroup(refgroupid, parentgroupid, groupname, groupsysname, description) values
(1, 0, 'ref group parent name 1', 'refgroupparentsysname1', 'description 1');

insert into ref_reference(referenceid, refgroupid, referencename, refsysname, description) values
(1, 1, 'ref name 1', 'refsysname1', 'description 1'),
(2, 1, 'ref name 2', 'refsysname2', 'description 2');

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
(15, 1, 'code15', 'name15', 'brief15', 'description 15'),
(16, 2, 'code1', 'name1', 'brief1', 'description 1'),
(17, 2, 'code2', 'name2', 'brief2', 'description 2'),
(18, 2, 'code3', 'name3', 'brief3', 'description 3'),
(19, 2, 'code4', 'name4', 'brief4', 'description 4'),
(20, 2, 'code5', 'name5', 'brief5', 'description 5'),
(21, 2, 'code6', 'name6', 'brief6', 'description 6'),
(22, 2, 'code7', 'name7', 'brief7', 'description 7'),
(23, 2, 'code8', 'name8', 'brief8', 'description 8'),
(24, 2, 'code9', 'name9', 'brief9', 'description 9'),
(25, 2, 'code10', 'name10', 'brief10', 'description 10'),
(26, 2, 'code11', 'name11', 'brief11', 'description 11'),
(27, 2, 'code12', 'name12', 'brief12', 'description 12'),
(28, 2, 'code13', 'name13', 'brief13', 'description 13'),
(29, 2, 'code14', 'name14', 'brief14', 'description 14'),
(30, 2, 'code15', 'name15', 'brief15', 'description 15');

insert into ref_itemitemlink(linkid, refitemid, lnkrefitemid) values
(1, 1, 16),
(2, 2, 17),
(3, 3, 18),
(4, 4, 19),
(5, 5, 20),
(6, 6, 21),
(7, 7, 22),
(8, 8, 23),
(9, 9, 24),
(10, 10, 25),
(11, 11, 26),
(12, 12, 27),
(13, 13, 28),
(14, 14, 29),
(15, 15, 30);

