---
title: 'Markdown Style Guide'
description: 'Here is a sample of some basic Markdown syntax that can be used when writing Markdown content in Astro.'
pubDate: '2099-12-31'
heroImage: '/blog-placeholder-1.jpg'
---

こちらは、AstroでMarkdownコンテンツを書く際に使用できる基本的なMarkdown構文のサンプルです。

## 見出し

以下のHTML `<h1>`～`<h6>` 要素は6つのセクションレベルの見出しを表します。`<h1>` が最上位のセクションレベル、`<h6>` が最下位のセクションレベルを表します。

# H1

## H2

### H3

#### H4

##### H5

###### H6

## 段落

Xerum, quo qui aut unt expliquam qui dolut labo. Aque venitatiusda cum, voluptionse latur sitiae dolessi aut parist aut dollo enim qui voluptate ma dolestendit peritin re plis aut quas inctum laceat est volestemque commosa as cus endigna tectur, offic to cor sequas etum rerum idem sintibus eiur? Quianimin porecus evelectur, cum que nis nust voloribus ratem aut omnimi, sitatur? Quiatem. Nam, omnis sum am facea corem alique molestrunt et eos evelece arcillit ut aut eos eos nus, sin conecerem erum fuga. Ri oditatquam, ad quibus unda veliamenimin cusam et facea ipsamus es exerum sitate dolores editium rerore eost, temped molorro ratiae volorro te reribus dolorer sperchicium faceata tiustia prat.

Itatur? Quiatae cullecum rem ent aut odis in re eossequodi nonsequ idebis ne sapicia is sinveli squiatum, core et que aut hariosam ex eat.

## 画像

### 構文

```markdown
![Alt text](./full/or/relative/path/of/image)
```

### 出力

![ブログのプレースホルダー](/blog-placeholder-about.jpg)

## 引用

`blockquote` 要素は、他のソースから引用された内容を表します。任意で、引用元を`footer` や `cite` 要素内に追加したり、注釈や略語などのインライン変更も追加できます。

### 引用 (出典なし)

#### 構文

```markdown
> Tiam, ad mint andaepu dandae nostion secatur sequo quae.  
> **Note** that you can use _Markdown syntax_ within a blockquote.
```

#### 出力

> Tiam, ad mint andaepu dandae nostion secatur sequo quae.  
> **Note** that you can use _Markdown syntax_ within a blockquote.

### 引用 (出典あり)

#### 構文

```markdown
> Don't communicate by sharing memory, share memory by communicating.<br>
> — <cite>Rob Pike[^1]</cite>
```

#### 出力

> Don't communicate by sharing memory, share memory by communicating.<br>
> — <cite>Rob Pike[^1]</cite>

[^1]: 上記の引用は、Rob Pikeが2015年11月18日のGopherfestで行った[講演](https://www.youtube.com/watch?v=PAAkCSZUG1c)からの抜粋です。

## テーブル

### 構文

```markdown
| Italics   | Bold     | Code   |
| --------- | -------- | ------ |
| _italics_ | **bold** | `code` |
```

### 出力

| Italics   | Bold     | Code   |
| --------- | -------- | ------ |
| _italics_ | **bold** | `code` |

## コードブロック

### 構文

3つのバッククォート ``` を新しい行で追加し、コードスニペットを記述して、もう一度3つのバッククォートで閉じます。コードのハイライトを行うには、最初の3つのバッククォートの後に言語名を記述します (例： html, javascript, css, markdown, typescript, txt, bash など)。

````markdown
```html
<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <title>Example HTML5 Document</title>
  </head>
  <body>
    <p>Test</p>
  </body>
</html>
```
````

### 出力

```html
<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <title>Example HTML5 Document</title>
  </head>
  <body>
    <p>Test</p>
  </body>
</html>
```

## リストの種類

### 順序付きリスト

#### 構文

```markdown
1. First item
2. Second item
3. Third item
```

#### 出力

1. First item
2. Second item
3. Third item

### 順序なしリスト

#### 構文

```markdown
- List item
- Another item
- And another item
```

#### 出力

- List item
- Another item
- And another item

### ネストされたリスト

#### 構文

```markdown
- Fruit
  - Apple
  - Orange
  - Banana
- Dairy
  - Milk
  - Cheese
```

#### 出力

- Fruit
  - Apple
  - Orange
  - Banana
- Dairy
  - Milk
  - Cheese

## その他の要素 — abbr, sub, sup, kbd, mark

### 構文

```markdown
<abbr title="Graphics Interchange Format">GIF</abbr> is a bitmap image format.

H<sub>2</sub>O

X<sup>n</sup> + Y<sup>n</sup> = Z<sup>n</sup>

Press <kbd>CTRL</kbd> + <kbd>ALT</kbd> + <kbd>Delete</kbd> to end the session.

Most <mark>salamanders</mark> are nocturnal, and hunt for insects, worms, and other small creatures.
```

### 出力

<abbr title="Graphics Interchange Format">GIF</abbr> is a bitmap image format.

H<sub>2</sub>O

X<sup>n</sup> + Y<sup>n</sup> = Z<sup>n</sup>

Press <kbd>CTRL</kbd> + <kbd>ALT</kbd> + <kbd>Delete</kbd> to end the session.

Most <mark>salamanders</mark> are nocturnal, and hunt for insects, worms, and other small creatures.
