-- 外键约束: Employee.dept_id -> dept.id
-- ON DELETE RESTRICT: 禁止删除仍有员工的部门
-- ON UPDATE CASCADE: 部门ID变化时自动同步员工表的 dept_id
--
-- 前置条件：确保 Employee 表中不存在孤立 dept_id
-- 如有孤立数据，先执行：
--   UPDATE `Employee` SET dept_id = NULL
--   WHERE dept_id IS NOT NULL AND dept_id NOT IN (SELECT id FROM dept);

ALTER TABLE `Employee` ADD CONSTRAINT fk_emp_dept
    FOREIGN KEY (dept_id) REFERENCES dept(id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE;
