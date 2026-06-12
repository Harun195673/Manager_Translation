package Reentry.first;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FirstApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirstApplication.class, args);
	}






	/*

	When u have delete or update with employee or task
	it must udoate in taskassignemnt too


====================================================
Exception Framework for This Program
====================================================

1. ResourceNotFoundException
----------------------------------------------------
Use this when something does not exist in the database.

Example situations:
- Employee not found
- Task not found
- Manager not found
- WorkGroup not found

Meaning:
"The program needs this object, but it cannot find it."

Typical HTTP status:
404 Not Found


2. DuplicateResourceException
----------------------------------------------------
Use this when something already exists, but should only exist once.

Example situations:
- Employee is already assigned to this task
- Email already exists
- Group name already exists
- Task with this title already exists

Meaning:
"The user is trying to create a duplicate."

Typical HTTP status:
409 Conflict


3. InvalidOperationException
----------------------------------------------------
Use this when the action itself is not allowed right now.

Example situations:
- Cannot assign employee to a completed task
- Cannot delete group while employees are still inside it /// Done
- Cannot edit a cancelled task							  /// Done
- Cannot remove manager from active group                 /// Done

Meaning:
"The data exists, but this action does not make sense in the current state."

Typical HTTP status:
400 Bad Request


4. BusinessValidationException
----------------------------------------------------
Use this when a business rule of the application is broken.

Example situations:
- Deadline cannot be before start date                    /// Done
- Employee cannot have more than 5 active tasks
- Task must have at least one employee before completion
- Completion date cannot be before assignment date

Meaning:
"The input may be technically valid, but it breaks a rule of the program."

Typical HTTP status:
400 Bad Request


5. ForbiddenOperationException
----------------------------------------------------
Use this when the user/manager is not allowed to perform the action.

Example situations:
- Manager tries to edit another manager's task
- Manager tries to delete a group they do not own
- Employee tries to access another employee's assignment

Meaning:
"The action might be valid, but this person has no permission."

Typical HTTP status:
403 Forbidden


====================================================
Short Memory Version
====================================================

ResourceNotFoundException  = missing data
DuplicateResourceException = already exists
InvalidOperationException  = invalid action
BusinessValidationException = broken business rule
ForbiddenOperationException = no permission

====================================================
Decision Framework
====================================================

Ask these questions inside service methods:

1. Does the required entity exist?
   -> no: ResourceNotFoundException

2. Does this already exist?
   -> yes: DuplicateResourceException

3. Does this action make sense in the current state?
   -> no: InvalidOperationException

4. Does this break an application/business rule?
   -> yes: BusinessValidationException

5. Is this user/manager allowed to do this?
   -> no: ForbiddenOperationException

====================================================
*/













}
