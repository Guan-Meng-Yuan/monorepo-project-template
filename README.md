# 前置步骤
## 更新前端模版代码
```
// 添加模版代码仓库地址
git remote add soybean https://github.com/soybeanjs/soybean-admin.git
// 嵌入到当前项目
git subtree add --prefix=frontend soybean example --squash
// 更新前端模版代码到项目
git subtree pull --prefix=frontend soybean example --squash

```
## 更新移动端模版代码

```
// 添加模版代码仓库地址
git remote add wot https://github.com/wot-ui/wot-starter.git
// 嵌入到当前项目
git subtree add --prefix=mobile wot main --squash
// 更新移动端模版代码到项目
git subtree pull --prefix=mobile wot main --squash

```