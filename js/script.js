const form = document.getElementById('uploadForm');
const imageInput = document.getElementById('imageInput');
const preview = document.getElementById('preview');

const captionsDiv = document.getElementById('captions');

imageInput.addEventListener('change', () => {
    const file = imageInput.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = e => {
            preview.src = e.target.result;
            preview.style.display = 'block';
        };
        reader.readAsDataURL(file);
    } else {
        preview.style.display = 'none';
    }
});

form.addEventListener('submit', async event => {
    event.preventDefault();

    captionsDiv.innerHTML = '';
    const formData = new FormData();
    formData.append('image', imageInput.files[0]);

    try {
        const response = await fetch('http://localhost:8080/api/images/generateCaption', {
            method: 'POST',
            body: formData
        });

        if (!response.ok) {
            throw new Error('Failed to upload image');
        }

        const captions = await response.json();
        displayCaptions(captions);
    } catch (error) {
        console.error('Error:', error);
        captionsDiv.innerHTML = `<div class="alert alert-danger">Error: ${error.message}</div>`;
        captionsDiv.style.display = 'block';
    }
});

function displayCaptions(captions) {
    captionsDiv.innerHTML = '';
    captions.forEach((caption) => {
        const listItem = document.createElement('li');
        listItem.className = 'list-group-item';
        listItem.textContent = `${caption}`;
        captionsDiv.appendChild(listItem);
    });
    captionsDiv.style.display = 'block';
}