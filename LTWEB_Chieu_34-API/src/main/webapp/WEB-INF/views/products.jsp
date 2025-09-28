<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="vi">
<head>
<meta charset="utf-8" />
<title>Products (GraphQL AJAX – JSP)</title>
<meta name="viewport" content="width=device-width, initial-scale=1" />
</head>
<body>
	<h1>Danh sách Product theo Category</h1>
	<label>Category ID: <input id="cid" type="number" value="1" /></label>
	<button id="btnLoadByCat">Load theo Category</button>
	<ul id="list"></ul>


	<script>
const GQL_ENDPOINT = "${pageContext.request.contextPath}/graphql";


async function gql(query, variables={}){
const res = await fetch(GQL_ENDPOINT,{
method:'POST',
headers:{'Content-Type':'application/json'},
body: JSON.stringify({query, variables})
});
const json = await res.json();
if(json.errors){ console.error(json.errors); alert('GraphQL error.'); }
return json.data;
}


document.getElementById('btnLoadByCat').addEventListener('click', async ()=>{
const cid = parseInt(document.getElementById('cid').value,10);
const query = `query($id:ID!){ productsByCategory(categoryId:$id){ id title price } }`;
const data = await gql(query,{id: cid});
const ul = document.getElementById('list');
ul.innerHTML='';
data.productsByCategory.forEach(p=>{
const li = document.createElement('li');
li.textContent = `${p.id} - ${p.title} - ${p.price}`;
ul.appendChild(li);
});
});
</script>
</body>
</html>