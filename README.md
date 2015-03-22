# skinny-orm-example
Runnable sample code for Skinny-ORM.

# TODO
TODO : 修正

# 実行結果
```
22:10:50.732 [main] DEBUG scalikejdbc.ConnectionPool$ - Registered connection pool : ConnectionPool(url:jdbc:h2:file:./db/development;MODE=MySQL;AUTO_SERVER=TRUE, user:sa) using factory : commons-dbcp
22:10:51.358 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] drop table if exists person; (6 ms)
22:10:51.410 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] drop table if exists company; (2 ms)
22:10:51.417 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] drop table if exists company_type; (5 ms)
22:10:51.424 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] create table company_type (id bigint primary key, name varchar(64)); (1 ms)
22:10:51.458 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] create table company (id bigint primary key, name varchar(64), company_type_id bigint, foreign key (company_type_id) references company_type(id)); (29 ms)
22:10:51.484 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] create table person (id bigint primary key, name varchar(64), company_id bigint, foreign key (company_id) references company(id)); (23 ms)
22:10:51.486 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] insert into company_type(id, name) values (1, 'IT'); (0 ms)
22:10:51.491 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] insert into company(id, name, company_type_id) values (1, 'Company A', 1); (1 ms)
22:10:51.498 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] insert into person(id, name, company_id) values (1, 'seratch_ja', 1); (1 ms)
22:10:51.503 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] insert into person(id, name, company_id) values (2, 's_tsuka', NULL); (0 ms)
### Start print ###
22:10:51.951 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] select person.id as i_on_person, person.name as n_on_person, person.company_id as ci_on_person , company.id as i_on_company, company.name as n_on_company, company.company_type_id as cti_on_company from person left join company on person.company_id = company.id order by person.id; (1 ms)
22:10:52.036 [main] DEBUG s.StatementExecutor$$anon$1 - [SQL Execution] select company.id as i_on_company, company.name as n_on_company, company.company_type_id as cti_on_company from company where company.id in (1); (0 ms)
Person(1,seratch_ja,Some(1),Some(Company(1,Company A,Some(1),None)))
Person(2,s_tsuka,None,None)
```

# 困っていること

１個目のPersonはCompanyを持っていて、そのCompanyはCompanyTypeを持っているのでCompanyTypeも取れてほしいけど、現状はNone

Eager loadingを使っていて、merge = (persons, companies)のcompanies内にCompanyTypeも入ってるそうだけど、デバッガーで見ても入っていない

上記の２個目のSQLもなんか違うような。使い方が間違っているかこのケースは「Note: eager loading of nested entities is not supported yet.」に当てはまるやつ・・・？
