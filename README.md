# Redis

## 初始化项目
```shell
Quick setup — if you’ve done this kind of thing before
or	
git@github.com:xconfigurator/test-data-redis.git
Get started by creating a new file or uploading an existing file. We recommend every repository include a README, LICENSE, and .gitignore.

…or create a new repository on the command line
echo "# test-data-redis" >> README.md
git init
git add README.md
git commit -m "first commit"
git branch -M main
git remote add origin git@github.com:xconfigurator/test-data-redis.git
git push -u origin main
…or push an existing repository from the command line
git remote add origin git@github.com:xconfigurator/test-data-redis.git
git branch -M main
git push -u origin main
…or import code from another repository
You can initialize this repository with code from a Subversion, Mercurial, or TFS project.
```

## 几个重要问题
1. 基础设施

2. 单独使用  
   RedisTemplate  
   StringRedisTemplate  
   基础还是Redis各种数据结构的操作练习

3. 在Spring cache abstraction场景下使用  
    注解
   
4. 配置序列化成JSON
    1. 单独使用，配置RedisTemplate。
    2. Spring cache abstraction场景下，配置。

