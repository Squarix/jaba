<%@ page %>
<html>
<head>
    <title>lab_10</title>
    <script src="jquery-3.2.1.js"></script>
    <link rel="stylesheet" href="index.css">
</head>
<body>

<div>
    <input id="password" type="password" placeholder="password"/>
    <button onclick="login()">Send</button>
    <button onclick="reset()">Reset</button>
</div>

<div id="posts">

</div>

<div id="new-post" class="new-post">
</div>

<script>
    $(document).ready(function () {
        getPosts();

        if (getCookie("isAdmin") === "true") {
            $('#new-post').html(`
              <h3 class="post-header">New post: </h3>
              <input type="text" class="input" id="link" placeholder="Link"/>
              <textarea type="text" class="input" id="tags" placeholder="Tags (split with ',')"></textarea>
              <button onClick="create()">Create</button>
            `);
        }
    });

    function login() {
        $.ajax({
            url: "./users",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({password: $('#password').val()}),
            success: result => {
                setCookie('isAdmin', true);
            },
            error: error => {
                console.log(error);
            }
        })
    }

    function reset() {
        $.ajax({
            url: "./users",
            type: "DELETE",
            success: result => {
                setCookie('isAdmin', false);
            },
            error: error => {
                console.log(error);
            }
        })
    }

    function create() {
        const link = $('#link').val();
        const tags = $('#tags').val().split(',').map(t => ({Name: t.toLowerCase()}));

        $.ajax({
            url: "./posts",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({post: {Link: link}, tags}),
            success: result => {
                getPosts();
            },
            error: error => {
                console.log(error);
            }
        })

    }

    function updateComment(id) {
        $.ajax({
            url: "./comments",
            type: "PUT",
            contentType: "application/json",
            data: JSON.stringify({Id: id, Text: $('#updateComment').val()}),
            success: result => {
                getPosts();
            },
            error: error => {
                console.log(error);
            }
        })
    }

    function deleteComment(id) {
        $.ajax({
            url: "./comments?id=" + id,
            type: "DELETE",
            contentType: "application/json",
            success: result => {
                getPosts();
            },
            error: error => {
                console.log(error);
            }
        })
    }

    function deletePost(id) {
        $.ajax({
            url: "./posts?postId=" + id,
            type: "DELETE",
            contentType: "application/json",
            success: result => {
                getPosts();
            },
            error: error => {
                console.log(error);
            }
        })
    }

    function tagClicked(tag) {
        getPosts(tag);
    }

    function getPosts(tag = '') {
        $.ajax({
            url: "./posts?" + (tag ? 'filter=' + tag : ''),
            type: "GET",
            success: result => {
                $("#posts").html(result);
            },
            error: error => {
                console.log(error);
            }
        })
    }

    function like(id) {
        $.ajax({
            url: "./likes",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({PostId: id}),
            success: result => {
                getPosts();
            },
            error: error => {
                console.log(error);
            }
        })
    }

    function dislike(id) {
        $.ajax({
            url: "./dislikes",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({PostId: id}),
            success: result => {
                getPosts();
            },
            error: error => {
                console.log(error);
            }
        })
    }

    function comments(classId) {
        const id = classId.slice(8, classId.length);
        $.ajax({
            url: "./comments?postId=" + id,
            type: "GET",
            success: result => {
                $('#' + classId).html(result + commentContainer(id));
                $('#create-button').click(e => createComment(id))
            },
            error: error => {
                console.log(error);
            }
        })
    }

    function createComment(id) {
        const Nickname = $(`#newCommentNN`).val();
        const Text = $(`#newComment`).val();
        $.ajax({
            url: "/comments",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({Nickname, Text, PostId: id}),
            success: result => {
                comments('comments' + id);
            },
            error: error => {
                console.log(error);
            }
        })
    }

    const commentContainer = id => `
      <div class="comment comment-container">
        <input placeholder="nickname" type="text" id="newCommentNN" />
        <textarea placeholder="comment" id="newComment"></textarea>
        <button id="create-button">Comment</button>
      </div>
    `;

    function setCookie(cname, cvalue, exdays) {
        var d = new Date();
        d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
        var expires = "expires=" + d.toUTCString();
        document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
    }

    function getCookie(cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
            var c = ca[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) == 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    }

</script>
</body>
</html>
