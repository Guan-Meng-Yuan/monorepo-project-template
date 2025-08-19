```bash

git remote add soybean-admin https://github.com/soybeanjs/soybean-admin.git

git fetch soybean-admin

git subtree pull --prefix frontend soybean-admin example --squash

```

### 添加模版项目地址，以 soybean-admin 为例

```shell
git remote add soybean-admin https://github.com/soybeanjs/soybean-admin.git

git fetch soybean-admin

git subtree add --prefix frontend soybean-admin example --squash
```

### 拉取代码

```shell
git subtree pull --prefix frontend soybean-admin example --squash
```
