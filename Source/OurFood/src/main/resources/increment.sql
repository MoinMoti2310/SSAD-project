-- CREATED <DATE> --



-- EXECUTED IN PROD -- 

-- 10/08/2016 --
ALTER TABLE quick_mark drop column marks;
ALTER TABLE action drop column is_quick_mark_enabled;
ALTER TABLE action drop column is_quick_mark_comment_enabled;
ALTER TABLE action_data MODIFY data_12 VARCHAR(255);
ALTER TABLE action_data MODIFY data_13 VARCHAR(255);