# Gitlet Design Document

**Name**:

## .gitlet structure
`.gitlet`
- commits(dir)
- blobs(dir)
- refs(dir) 存放引用的分支信息
- HEAD 对HEAD的引用信息
- STAGE 对STAGE信息的存储
> 注意blobs里面类比git存储，套了一层文件夹


## Classes and Data Structures

### Main

Just to determain what kind of operation will do.



   
### Repository 

#### Fields
1. 目录存放的常量
```java
public static final File CWD;
public static final File GITLET_DIR;
public static final File HEAD_PATH = join(GITLET_DIR, "HEAD");
public static final File STAGE_FILE = join(GITLET_DIR, "STAGE");
public static final File COMMIT_DIR = join(GITLET_DIR, "COMMIT");
public static final File BLOB_DIR = join(GITLET_DIR, "BLOB");
```

2. 其他变量
private boolean isInitialized 判断是否已经初始化过了
public String HEAD 当前HEAD的标识
#### Methods
public static void init() 初始化


### Commit
#### Fields
```java
/** The message of this Commit. */
private String message;
/** The parent commit */
private Commit parent;
/** The commit datatime */
private Date date;
/** The snapshot Tree */
private Map<String, String> snapshot;
```
#### Methods


## Stage
### Fields


## Algorithms

## Persistence

