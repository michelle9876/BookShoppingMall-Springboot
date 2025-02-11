# 서적 쇼핑몰 서비스
2차 프로젝트 쇼핑몰 생성에 맞춰 소설 도서 판매 사이트를 생성하고자 함.

## 1. 목표와 기능
### 1.1 목표
- FE/BE 협업하여 도서 쇼핑몰 생성
- 유저 회원가입/로그인/도서 목록(메인)/마이페이지/장바구니/결제 기능 및 페이지 구현

### 1.2 기능
- 회원관리
  - 회원가입/로그인
  - 개인정보관리
  - 주문관리
  - 장바구니
- 도서 검색 및 분류
  - 카테고리 검색 필터링
  - 도서 상세정보 제공
- 결제 시스템
  - 카드결제 외부 api 도입하여 결제 진행

### 1.3 팀 구성
- 김현지
- 김보경
- 김도현
- 우비주


## 2. 개발 환경 및 배포 URL
### 2.1 개발 환경
- Spring Boot 3.4.0
- JAVA 17
- MariaDB 10.11
- JPA
- AWS, S3, EC2


### 2.2 배포 URL
- [FE 배포 URL : bookshoppingmall.vercel.app](bookshoppingmall.vercel.app)
- [BE 배포 URL : https://project-be.site](https://project-be.site)
<!--
- 테스트용 계정
  ※ 판매자,구매자 둘다 작성해야합니다.
    ```
    id : test@test.test
    pw : test11!!
    ```
-->

### 2.3 URL 구조- API 
[📒 API 명세서 참고](https://far-cormorant-298.notion.site/API-1667279b2a39807081fad1cc3eb3260c?pvs=4)
|App|Method|URL|Note|Authentication|
|:---:|:---:|---|---|:---:|
|auth|POST|'/auth/signup'|회원가입||
|auth|POST|'/auth/login'|로그인||
|auth|POST|'/auth/email'|이메일 중복체크||
|auth|GET|'/auth/delete'|회원탈퇴|✅|
|mypage|GET|'/api/mypage/getUserInfo'|유저정보 조회|✅|
|mypage|PUT|'/api/mypage/putUserInfo/{id}'|유저정보 수정|✅|
|mypage|GET|'/api/mypage/getPaymentList'|결제내역 목록 조회|✅|
|mypage|GET|'/api/mypage/getPaymentDetail/{id}'|결제내역 상세 조회|✅|
|mypage|GET|'/api/mypage/getCartItems'|장바구니 목록 조회|✅|
|mypage|PUT|'/api/mypage/putCartOption'|장바구니 옵션 수정|✅|
|mypage|DELETE|'/api/mypage/deleteCartItems'|장바구니 삭제|✅|
|cart|POST|'/cart/add'|장바구니 담기|✅|
|books|GET|'/books'|도서 전체 조회||
|books|GET|'/books/{id}'|도서 상세 조회||
|books|GET|'/books/category/{category}'|도서 카테고리로 조회||
|payments|POST|'/payments/process'|결제|✅|


## 3. 개발 일정
| 기획 및 설계 | 2일 |
| :--- | :---: |
| 프론트엔드 개발 | 10일 |
| 백엔드 개발 | 10일 |
| 테스트 및 디버깅 | 1일 |
| 배포 및 안정화 | 3일 |

#### 데일리 스크럼 시간 : 매일 오후 2시

## 4. 데이터베이스 모델링(ERD)
![ERD](https://file.notion.so/f/f/b8bd0386-e204-4999-9ab0-88889c0933a6/6b3bbe67-64a8-4182-a4ea-d0c698d0073b/ERD-BookShoppingMall_(2).png?table=block&id=1627279b-2a39-80e6-a1c0-d7b0261c86cf&spaceId=b8bd0386-e204-4999-9ab0-88889c0933a6&expirationTimestamp=1739296800000&signature=L2RVPDEDoif1VnqD5xZBgObTdOjagI-st5-Y419E3fc&downloadName=ERD-BookShoppingMall+%282%29.png)





<!-- 아래는 원본내용입니다.
 # 서적 쇼핑몰 서비스

## 1. 목표와 기능
### 1.1 목표
- 유저 로그인/로그아웃
- 쇼핑몰 물품 조회/구매
- 쇼핑몰 물품 판매
- 유저 마이페이지

### 1.2 기능
#### \[F.E]
로그인
- 회원 가입 시 필요한 데이터 정보 submit & 유효성 검사
- 이메일과 비밀번호를 입력하여 로그인 기능 구현 및 유효성 검사
- 로그아웃
  물품 조회/구매
- 이미지와 상품명, 옵션, 가격 등의 정보를 그리드 형태로 리스트로 나타낸다.
- 옵션에 따른 필터 및 페이지네이션 기능을 구현한다.
- 이미지와 상품명, 옵션, 가격 등의 정보를 나타낸다.
- 장바구니에 수량, 옵션을 설정해서 담을 수 있는 버튼을 구현한다.
- 장바구니에 담긴 물품 내역을 보여준다.
- 장바구니에 담긴 물품 내역을 수정할 수 있으며 그에 따라 가격이 달라진다.
- 결제에 필요한 정보를 입력해서 주문 기능을 구현한다.
  물품 판매
- 판매할 물품을 등록 할 수 있게 한다.
- 판매중인 물품의 재고를 수정할 수 있다.
- 판매 완료/판매 중인 물품 내역을 조회할 수 있다.
  마이 페이지
- 유저 정보를 보여준다.
- 유저가 장바구니에 담은 물품 리스트를 보여준다.
- 유저가 구매한 물품 리스트를 보여준다.

#### \[B.E]
-
-

### 1.3 팀 구성

#### \[F.E]팀원 소개
- 윤주연
- 허민주
- 박영욱
- 김동현
- 이셀라
- 박조은

#### \[B.E]팀원 소개
- 김현지
- 김보경
- 김도현
- 우비주


## 2. 개발 환경 및 배포 URL
### 2.1 개발 환경 ※ 예시일뿐 개발에 사용한 프레임워크,API만 작성하면 됩니다.

#### \[F.E]

- Web Framework
    - Django 3.x (Python 3.8)
- 서비스 배포 환경
    - Amazon Lightsail ...

#### \[B.E]

- Web Framework
    - Django 3.x (Python 3.8)
- 서비스 배포 환경
    - Amazon Lightsail ...

### 2.2 배포 URL

- 배포할 URL 여기에 작성해주시면 됩니다.
- 테스트용 계정
  ※ 판매자,구매자 둘다 작성해야합니다.
- 단, 중고 거래인 경우 하나만 작성해도 괜찮습니다.
    ```
    id : test@test.test
    pw : test11!!
    ```


### 2.3 \[F.E] URL 구조 ※ 예시로 작성된 URL입니다.

- main

|App|URL|Views Function|HTML File Name|Note|
|---|---|---|---|---|
|main|'/'|home|main/home.html|홈화면|
|main|'/about/'|about|main/about.html|소개화면|

- accounts

|App|URL|Views Function|HTML File Name|Note|
|---|---|---|---|---|
|accounts|'register/'|register|accounts/register.html|회원가입|
|accounts|'login/'|login|accounts/login.html|로그인|
|accounts|'logout/'|logout|accounts/logout.html|로그아웃|
|accounts|'profile/'|profile|accounts/profile.html|비밀번호변경기능 /  <br>프로필 수정/ 닉네임추가|

- boardapp

|App|URL|Views Function|HTML File Name|Note|
|---|---|---|---|---|
|board|'board/'|board|boardapp/post_list.html|게시판 목록|
|board|'board/int:pk/'|post_detail|boardapp/post_detail.html|게시글 상세보기|
|board|'board/write/'|post_write|boardapp/post_write.html|게시글 작성|
|board|'board/edit/int:pk/'|post_edit|boardapp/post_edit.html|게시글 수정|
|board|'board/delete/int:pk/'|post_delete|boardapp/post_delete.html|게시글 삭제|
|board|'board/int:pk/comment/'|comment_create|boardapp/comment_form.html|댓글 작성|
|board|'board/int:pk/comment/  <br>int:comment_pk/edit/'|comment_edit|boardapp/comment_form.html|댓글 수정|
|board|'board/int:pk/comment/  <br>int:comment_pk/delete/'|comment_delete|boardapp/comment_  <br>confirm_delete.html|댓글 삭제|

- blog

|App|URL|Views Function|HTML File Name|Note|
|---|---|---|---|---|
|blog|'blog/'|blog|blog/blog.html|갤러리형 게시판 메인 화면|
|blog|'blog/int:pk/'|post|blog/post.html|상세 포스트 화면|
|blog|'blog/write/'|write|blog/write.html|카테고리 지정, 사진업로드,  <br>게시글 조회수 반영|
|blog|'blog/edit/int:pk/'|edit|blog/edit.html|게시물목록보기|
|blog|'blog/delete/int:pk/'|delete|blog/delete.html|삭제 화면|
|blog|'blog/search/'|search|blog/search.html|주제와 카테고리에 따라 검색,  <br>시간순에 따라 정렬|
|blog|'post/int:post_pk/comment/'|comment_new|blog/comment_form.html|댓글 입력 폼|
|blog|'post/int:post_pk/comment/  <br>int:parent_pk/'|reply_new|blog/comment_form.html|대댓글 폼|
|blog|'post/int:pk/like/'|like_post|blog/post.html|좋아요를 누르면 blog/post로 Redirect됨|
|blog|'comment/int:pk/update/'|comment_update|blog/comment_form.html|댓글 업데이터 경로|
|blog|'comment/int:pk/delete/'|comment_delete|blog/comment_  <br>confirm_delete.html|댓글 삭제 폼|

### 2.4 \[B.E] URL 구조

|App|Method|URL|Views Class|Note|Authentication|
|---|---|---|---|---|---|
|blog|GET|'/blog/posts/'|PostViewSet|게시글 목록||
|blog|POST|'/blog/posts/'|PostViewSet|게시글 생성 / ChatGPT API 요청|✅|
|blog|GET|'/blog/posts/{post_id}/'|PostViewSet|게시글 상세보기 / 게시글 조회수 증가||
|blog|PATCH|'/blog/posts/{post_id}/'|PostViewSet|게시글 수정||
|blog|DELETE|'/blog/posts/{post_id}/'|PostViewSet|게시글 삭제||
|blog|POST|'/blog/posts/{post_id}/like/'|PostViewSet|게시글 좋아요 증가||
|blog|GET|'/blog/posts/{post_id}/comments/'|CommentViewSet|게시물의 댓글 목록||
|blog|POST|'/blog/posts/{post_id}/comments/'|CommentViewSet|게시물의 댓글 생성||
|blog|GET|'/blog/posts/{post_id}/comments/{comment_id}/'|CommentViewSet|게시물의 특정 댓글 보기||
|blog|PATCH|'/blog/posts/{post_id}/comments/{comment_id}/'|CommentViewSet|게시물의 특정 댓글 수정||
|blog|DELETE|'/blog/posts/{post_id}/comments/{comment_id}/'|CommentViewSet|게시물의 특정 댓글 삭제||



## 3. 요구사항 명세와 기능 명세

- [https://www.mindmeister.com/](https://www.mindmeister.com/) 등을 사용하여 모델링 및 요구사항 명세를 시각화하면 좋습니다.
- 이미지는 셈플 이미지입니다.

[![](https://github.com/weniv/project_sample_repo/raw/main/map.png)](https://github.com/weniv/project_sample_repo/blob/main/map.png)

- 머메이드를 이용해 시각화 할 수 있습니다.

## 4. 개발 일정

## 데일리 스크럼 시간 : 매일 오후 2시

### 4.1 개발 일정(WBS)


[![](https://github.com/weniv/project_sample_repo/raw/main/wbs_xlsx.png)](https://github.com/weniv/project_sample_repo/blob/main/wbs_xlsx.png)

- 좀 더 가벼운 프로젝트는 아래 일정표를 사용하세요.
- 아래 일정표는 [habitmaker.co.kr](https://habitmaker.co.kr/) 에서 작성되었습니다.
- 관련된 스택 표시는 [dev.habitmaker.co.kr](https://dev.habitmaker.co.kr/) 에서 작성되었습니다.


## 5. 와이어프레임 / UI / BM
### 5.1 와이어프레임
- 아래 페이지별 상세 설명, 더 큰 이미지로 하나하나씩 설명 필요

[![](https://github.com/weniv/project_sample_repo/raw/main/ui.png)](https://github.com/weniv/project_sample_repo/blob/main/ui.png)

- 와이어 프레임은 디자인을 할 수 있다면 '피그마'를, 디자인을 할 수 없다면 '카카오 오븐'으로 쉽게 만들 수 있습니다.

### 5.2 화면 설계 ※ 화면은 gif,jpg 파일로 업로드해주세요.

- 표는 가로/세로 상관이 없습니다.

|   |   |
|---|---|
|메인|로그인|
|[![](https://github.com/weniv/project_sample_repo/raw/main/ui1.png)](https://github.com/weniv/project_sample_repo/blob/main/ui1.png)|[![](https://github.com/weniv/project_sample_repo/raw/main/ui2.png)](https://github.com/weniv/project_sample_repo/blob/main/ui2.png)|
|회원가입|정보수정|
|[![](https://github.com/weniv/project_sample_repo/raw/main/ui3.png)](https://github.com/weniv/project_sample_repo/blob/main/ui3.png)|[![](https://github.com/weniv/project_sample_repo/raw/main/ui3.png)](https://github.com/weniv/project_sample_repo/blob/main/ui3.png)|
|검색|번역|
|[![](https://github.com/weniv/project_sample_repo/raw/main/ui3.png)](https://github.com/weniv/project_sample_repo/blob/main/ui3.png)|[![](https://github.com/weniv/project_sample_repo/raw/main/ui3.png)](https://github.com/weniv/project_sample_repo/blob/main/ui3.png)|
|선택삭제|글쓰기|
|[![](https://github.com/weniv/project_sample_repo/raw/main/ui3.png)](https://github.com/weniv/project_sample_repo/blob/main/ui3.png)|[![](https://github.com/weniv/project_sample_repo/raw/main/ui3.png)](https://github.com/weniv/project_sample_repo/blob/main/ui3.png)|
|글 상세보기|댓글|
|[![](https://github.com/weniv/project_sample_repo/raw/main/ui3.png)](https://github.com/weniv/project_sample_repo/blob/main/ui3.png)|[![](https://github.com/weniv/project_sample_repo/raw/main/ui3.png)](https://github.com/weniv/project_sample_repo/blob/main/ui3.png)|

## 7. 데이터베이스 모델링(ERD) 및 변수명

- 프론트와 백엔드 간에 정보를 주고 받는 과정에서 혼선이 일어나지 않도록 변수명을 설정이 필요합니다.
- 프론트에서 submit과정을 진행할 때 어떤 변수명으로 보내는지 알아야 데이터 맞추기가 수월합니다.

- 아래 ERD는 [ERDCloud](https://www.erdcloud.com/)를 사용했습니다.

[![](https://github.com/weniv/project_sample_repo/raw/main/erd.png)](https://github.com/weniv/project_sample_repo/blob/main/erd.png)

- [https://dbdiagram.io/home도](https://dbdiagram.io/home%EB%8F%84) 많이 사용합니다.
-->
## 8. Architecture

- 아래 Architecture 설계도는 PPT를 사용했습니다.

[![image](https://github.com/weniv/project_sample_repo/raw/main/architecture.png)](https://github.com/weniv/project_sample_repo/blob/main/architecture.png)

- PPT로 간단하게 작성하였으나, 아키텍쳐가 커지거나, 상세한 내용이 필요할 경우 [AWS architecture Tool](https://online.visual-paradigm.com/ko/diagrams/features/aws-architecture-diagram-tool/)을 사용하기도 합니다.

## 9. 메인 기능
- 머메이드나 이미지 파일을 사용하면 좋을 것 같습니다.
- 요구사항 및 기능 명세에서 제외한 기능을 작성하면 좋을 것 같습니다.
- 예를 들어, 이미지 저장을 위해 Amazon S3를 사용했다. OAuth 기능 구현을 위해 카카오로그인 API를 사용했다. 등등
- 요구사항 및 기능 명세에서 간단하게 설명만 하였다면 구현한 API를 자세하게 설명해도 괜찮습니다.
