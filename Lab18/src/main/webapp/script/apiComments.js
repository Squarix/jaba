function getCommentForReference(id) {
    fetch('/Lab18_war/Comments?refId='+id, {
        method: 'GET'
    }).then(res => res.json()).then(res => {
        let data = `<table>
                    <tr><td>
                        <h1>--UWSR COMMENTS/${id}--</h1>
                        <input type="button" onclick="visibilityCommentsInsertForm(${id})" value="insert">
                        <input type="button" onclick="clearContent(${id})" value="Close comments">
                        <div id="insertComment${id}"></div>
                    </td></tr>
                </table><br>`
        if (res) {
            res.forEach(element =>{
                let comments = `<table><tr><td>[${element.stamp}]`
                if(role == 'admin' || element.sessionId == sessionId)
                    comments += `<input type="button" value="delete" onclick="deleteComment(${element.id},${element.refId})">
                                 <input type="button" value="update" onclick="updateComment(${element.id},${element.refId})"><br>
                                 <textarea id="txt${element.id}">${element.comment}</textarea>`
                else
                    comments+= `<br><textarea readonly id="txt${element.id}">${element.comment}</textarea>`
                comments+= `</td></tr></table><br>`
                data += comments;
            })
        }
        document.getElementById(id).innerHTML = data;
    })
}


function addComment(id) {
    fetch('/Lab18_war/Comments', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            comment: document.getElementById('comment').value,
            refId: id,
            sessionId : sessionId
        })
    }).then(()=>{
        clearContent(id);
        getCommentForReference(id);
    })
}

function updateComment(id,refId) {
    let needId = 'txt'+id;
    fetch('/Lab18_war/Comments', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id: id,
            comment: document.getElementById(needId).value
        })
    }).then(()=>{
        getCommentForReference(refId);
    })
}

function deleteComment(id,refId) {
    fetch('/Lab18_war/Comments?id='+id,{
        method : 'DELETE'
    }).then(()=>{
        getCommentForReference(refId);
    })
}