/*
 * =====================================================
 * Backend-Konfiguration
 * =====================================================
 */

/*
const BACKEND_URL = "http://localhost:8080";
 */

const BACKEND_URL = "https://manager-translation.onrender.com";

const USERNAME = "matthias";
const PASSWORD = "password";

const MANAGER_ID = 2;
const WORK_GROUP_ID = 2;
const TASK_ID = 3;
const TASK_ASSIGNMENT_ID = 4;

const DEADLINE = "2030-06-15";

const TASK_ASSIGNMENT_NAME =
    "Frontend Demo Assignment";


const UPDATE_TASK_URL =
    `${BACKEND_URL}/tasks/${TASK_ID}`;

const WORKFLOW_URL =
    `${BACKEND_URL}/workflow/translate-and-assign`;


/*
 * =====================================================
 * Seite erkennen und passende Funktion starten
 * =====================================================
 */

document.addEventListener("DOMContentLoaded", () => {

    /*
     * Existiert auf index.html.
     */
    const taskForm =
        document.getElementById("task-form");

    if (taskForm) {
        taskForm.addEventListener(
            "submit",
            handleTaskForm
        );
    }


    /*
     * Existiert auf result.html.
     */
    const resultContainer =
        document.getElementById(
            "translated-task-results"
        );

    if (resultContainer) {
        displayStoredWorkflowResults();
    }
});


/*
 * =====================================================
 * Formular auf index.html verarbeiten
 * =====================================================
 */

async function handleTaskForm(event) {
    event.preventDefault();

    const titleInput =
        document.getElementById("task-title");

    const descriptionInput =
        document.getElementById(
            "task-description"
        );

    const submitButton =
        document.getElementById(
            "submit-button"
        );

    const taskMessage =
        document.getElementById(
            "task-message"
        );


    const title =
        titleInput.value.trim();

    const description =
        descriptionInput.value.trim();


    if (title === "" || description === "") {
        taskMessage.textContent =
            "Bitte Titel und Beschreibung eingeben.";

        return;
    }


    submitButton.disabled = true;

    submitButton.textContent =
        "Aufgabe wird verarbeitet...";

    taskMessage.textContent =
        "Aufgabe wird gespeichert...";


    try {

        /*
         * Schritt 1:
         * Task 3 mit den Formulardaten aktualisieren.
         */
        const updatedTask =
            await updateTask(
                title,
                description
            );

        console.log(
            "Task 3 wurde aktualisiert:",
            updatedTask
        );


        taskMessage.textContent =
            "Aufgabe gespeichert. Übersetzung wird gestartet...";


        /*
         * Schritt 2:
         * Übersetzungs- und Zuweisungsworkflow starten.
         */
        const workflowResults =
            await executeWorkflow();


        console.log(
            "Workflow-Ergebnisse:",
            workflowResults
        );


        /*
         * Schritt 3:
         * Ergebnisse für result.html speichern.
         */
        sessionStorage.setItem(
            "workflowResults",
            JSON.stringify(workflowResults)
        );


        /*
         * Formulareingaben zusätzlich speichern.
         */
        sessionStorage.setItem(
            "submittedTaskTitle",
            title
        );

        sessionStorage.setItem(
            "submittedTaskDescription",
            description
        );


        /*
         * Schritt 4:
         * Ergebnisseite öffnen.
         */
        window.location.href =
            "result.html";

    } catch (error) {

        console.error(
            "Verarbeitung fehlgeschlagen:",
            error
        );

        taskMessage.textContent =
            error.message;

    } finally {

        submitButton.disabled = false;

        submitButton.textContent =
            "Aufgabe erstellen und zuweisen";
    }
}


/*
 * =====================================================
 * Task 3 aktualisieren
 * =====================================================
 */

async function updateTask(title, description) {

    const response = await fetch(
        UPDATE_TASK_URL,
        {
            method: "PUT",

            headers: {
                "Content-Type":
                    "application/json",

                "Authorization":
                    createAuthorizationHeader()
            },

            /*
             * Entspricht deinem RequestTaskDTO:
             *
             * title
             * message
             * managerId
             * taskAssignmentId
             */
            body: JSON.stringify({
                title: title,
                message: description,
                managerId: MANAGER_ID,
                taskAssignmentId:
                    TASK_ASSIGNMENT_ID
            })
        }
    );


    if (!response.ok) {

        const errorText =
            await response.text();

        throw new Error(
            `Task konnte nicht aktualisiert werden. ` +
            `HTTP ${response.status}: ${errorText}`
        );
    }


    /*
     * Der Controller gibt RespondTaskDTO zurück.
     */
    return await response.json();
}


/*
 * =====================================================
 * Übersetzungsworkflow ausführen
 * =====================================================
 */

async function executeWorkflow() {

    const response = await fetch(
        WORKFLOW_URL,
        {
            method: "POST",

            headers: {
                "Content-Type":
                    "application/json",

                "Authorization":
                    createAuthorizationHeader()
            },

            /*
             * Die IDs bleiben immer gleich.
             *
             * Task 3 wurde vorher bereits mit den
             * Eingaben des Benutzers aktualisiert.
             */
            body: JSON.stringify({
                managerId: MANAGER_ID,
                workGroupId: WORK_GROUP_ID,
                taskId: TASK_ID,
                deadline: DEADLINE,
                taskAssignmentName:
                    TASK_ASSIGNMENT_NAME
            })
        }
    );


    if (!response.ok) {

        const errorText =
            await response.text();

        throw new Error(
            `Workflow konnte nicht ausgeführt werden. ` +
            `HTTP ${response.status}: ${errorText}`
        );
    }


    /*
     * Java:
     * List<RespondTaskAssignmentDTO>
     *
     * JavaScript:
     * Array von Objekten
     */
    const respondDtoList =
        await response.json();


    /*
     * Nur die im Frontend benötigten Werte übernehmen.
     */
    return respondDtoList.map(dto => ({
        id: dto.id,
        title: dto.taskTitle,
        translatedTask:
            dto.translatedTask,
        employeeName:
            dto.employeeName,
        assignmentName:
            dto.name,
        status:
            dto.status,
        deadline:
            dto.deadline
    }));
}


/*
 * =====================================================
 * Ergebnisse auf result.html anzeigen
 * =====================================================
 */

function displayStoredWorkflowResults() {

    const taskContainer =
        document.getElementById(
            "translated-task-results"
        );

    const resultMessage =
        document.getElementById(
            "result-message"
        );


    const storedResults =
        sessionStorage.getItem(
            "workflowResults"
        );


    if (!storedResults) {

        resultMessage.textContent =
            "Es wurden keine Workflow-Ergebnisse gefunden.";

        return;
    }


    let workflowResults;


    try {

        workflowResults =
            JSON.parse(storedResults);

    } catch (error) {

        console.error(
            "Gespeicherte Ergebnisse konnten nicht gelesen werden:",
            error
        );

        resultMessage.textContent =
            "Die Ergebnisse konnten nicht gelesen werden.";

        return;
    }


    if (
        !Array.isArray(workflowResults) ||
        workflowResults.length === 0
    ) {

        resultMessage.textContent =
            "Der Workflow hat keine Aufgaben zurückgegeben.";

        return;
    }


    /*
     * Nur die rechte Spalte wird geleert.
     *
     * Die Mitarbeitenden auf der linken Seite
     * bleiben fest im HTML.
     */
    taskContainer.innerHTML = "";

    resultMessage.textContent = "";


    workflowResults.forEach(task => {

        insertTranslatedTaskCard(
            taskContainer,
            task
        );
    });
}


/*
 * =====================================================
 * Übersetzte Aufgabenkarte erzeugen
 * =====================================================
 */

function insertTranslatedTaskCard(
    container,
    task
) {

    const translatedTaskElement =
        document.createElement("div");

    translatedTaskElement.classList.add(
        "translated_task"
    );


    const employeeName =
        task.employeeName ??
        "Unbekannter Mitarbeiter";

    const language =
        getEmployeeLanguage(
            employeeName
        );


    translatedTaskElement.innerHTML = `
        <div class="task_language">
            ${escapeHtml(language)}
        </div>

        <h3>
            ${escapeHtml(task.title)}
        </h3>

        <p>
            ${escapeHtml(task.translatedTask)}
        </p>

        <div class="assigned_to">
            Zugewiesen an:
            ${escapeHtml(employeeName)}
        </div>
    `;


    container.appendChild(
        translatedTaskElement
    );
}


/*
 * =====================================================
 * Sprache anhand des Demo-Mitarbeiters bestimmen
 * =====================================================
 */

function getEmployeeLanguage(employeeName) {

    const normalizedName =
        String(employeeName)
            .toLowerCase();


    if (
        normalizedName.includes(
            "mitarbeiter 1"
        )
    ) {
        return "Englisch";
    }


    if (
        normalizedName.includes(
            "mitarbeiter 2"
        )
    ) {
        return "Türkisch";
    }


    return "Unbekannt";
}


/*
 * =====================================================
 * Basic Authentication
 * =====================================================
 */

function createAuthorizationHeader() {

    return (
        "Basic " +
        btoa(
            `${USERNAME}:${PASSWORD}`
        )
    );
}


/*
 * =====================================================
 * Werte sicher in HTML einfügen
 * =====================================================
 */

function escapeHtml(value) {

    const temporaryElement =
        document.createElement("div");

    temporaryElement.textContent =
        value ?? "";

    return temporaryElement.innerHTML;
}