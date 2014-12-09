# Tetris Sprint
## Main Classes
- `Game`
  - This is the main class from which the Game is launch. Here we instantiate
  a new `AppController`, which controls the entire application.
- `AppController`
  - This is the controller for all parts of the game. It contains the
    necessary models, views, and controllers to communicate with the tetris
    game, the navigation bar, the statistics view, the high scores view,
    the help view, et cetera. It uses a CardLayout to do the switching between
    views. The main purpose of this class is to control the application at a
    high level; if you click the play button in the navigation bar, it should
    communicate with this controller to instantiate a new game and make the
    game view visible; if you submit a high score in the statistics view, that
    should get routed back to the `AppController`, which will then communicate
    with the `HighScoresModel` to post the score to the database, and then
    display the `HighScoresView`, which gets its data from `HighScoresModel`;
    and so on and so forth.
- `NavigationBar`
  - This class contains the navigation bar. It communicates with the
    `AppController` to act on the events that occur, and the
    `AppController` also communicates with it to keep its buttons in the
    appropriate state.
- `HomePanel`
  - This is the panel that the `AppController` displays when the application
    starts up. All it has is a play button at the time of this writing. in the
    future it will maybe have an icon or a title or something in addition to
    that. Even further in the future maybe it could have other game modes.
    This panel exists 1) in preparation for those things, and 2) because
    you need to be rerouted somewhere when you abort from a game. And
    3) because the
    [Quinn tetris game](http://boingboing.net/2006/07/09/mac-tetris-game-kill.html)
    has a main screen with just a play button on it, and I copied the idea
    from them. I also copied their idea of pausing the game when the window
    loses focus.
- `HelpWindow`
  - The `HelpWindow` is a `JFXPanel` that contains game instructions in HTML
    format. The HTML and CSS are located in the `/help` directory.

## Tetris Game Classes
- `TetrisController`
  - This class has references to a `TetrisModel` and a `TetrisView`, which act
    as the model and view, respectively, for the tetris game. It manages things
    like the game timers, performs the `Action` objects stored in the model on
    each frame update, and is responsible for playing/pausing and
    beginning/ending each game.
- `TetrisModel`
  - This stores every piece of data associated with the state of the tetris
    game. It has a bunch of methods that ensure that transformation to the
    tetris data maintain the tetris game 'invariant'. So for example, it makes
    sure invalid rotations cannot occur, and if they do, either corrects them
    or disallows that particular rotation. It also does things like lock minos
    into the matrix, compute lines clears, keep track of all the game
    statistics, update the game status, keep track of `Action`s that need to
    be performed, and fires property change events to its listener objects.
- `TetrisView`
  - This is the view for the entire tetris game. It contains subviews that
    display the game matrix, the hold queue, the next queue, the timer, the
    lines-remaining panel, and the like. It also takes in key events, which
    the model decodes (using a `KeyBinder` object) into `Action`s.

- `KeyBinder`
  - This is a useful class for encapsulating the key bindings data. The
    `TetrisModel` uses a `KeyBinder` to associate keys with `Action`s. This
    class was created because it will make it much easier to code custom
    key bindings in the future, in case anyone ever wanted to do that.

## Matrix Classes
- `MatrixModel`
  - This class is contains all of the data associated with the tetris matrix;
    this includes a matrix of `Color`'s, and the current mino and ghost.
    The `TetrisModel` delegates to this class all of the logic associated
    with translating/rotating the mino around the matrix, doing line-clears,
    and the line.
- `MatrixView`
  - A `MatrixView` is used as a subview inside of the main `TetrisView`.
    This class has a reference to a `MatrixModel` and is responsible for
    drawing the tetris matrix on the screen.

## Mino Classes
- `MinoType`
  - This is an enumeration that defines each mino by name, color, and default
    coordinates. It also includes a method for generating a random mino type,
    which is useful when in the `Mino` class we want to instantiate a random
    mino.
- `Mino`
  - This class represents a tetromino. It contains the mino's type, color,
    and coordinates, and has methods for rotating/translating the mino and
    for drawing the mino.
- `MinoPanel`
  - This is a utility class that we use to draw minos that are centered in
    rectangles on the screen. The hold queue and the next queue use instances
    of `MinoPanel` behind the scenes.
- `Ghost`
  - This class really just exists because it's semantically useful to
   differentiate between ghosts, which can't move around freely and exist to
   mirror actual minos, and minos, which should be able to translate/rotate
   independently. They're also drawn slightly differently.

## Next Queue Classes
- `QueueModel`
  - The `TetrisModel` delegates to the `QueueModel` all the logic related to
    the next queue. So, for example, it encapsulates logic to pop minos off
    the queue, maintaining the invariant that each time one is popped, another
    random mino should be added to the end of the queue, and the like.
- `QueueView`
  - A `QueueView` is a view for a `QueueModel`. This implementation uses
    `MinoPanel`s behind the scenes to draw the minos in the queue. When the
    model changes, property change events trigger the `QueueView` to
    re-render.

## Timer Classes
- `TimerModel`
  - This class really just contains the time in milliseconds. I claim it is a
    useful class because it separates the logic associated with computing the
    time string, which is used in multiple places, from everything else.
- `TimerView`
  - This class displays the a `TimerModel`'s time centered inside a panel in
    nice Helvetica font.

## Tetris Enums
- `Action`
  - This enumeration contains the following values:
    `MOVE_LEFT, MOVE_RIGHT, HOLD, HARD_DROP, SOFT_DROP, ROTATE_CLOCKWISE,`
    `ROTATE_COUNTER_CLOCKWISE, LOCK_MINO, TOGGLE_PAUSE.`
  - This enum is useful because it lets us store pending actions in the model,
    which the controller can execute on each frame update.
- `GameStatus`
  - The `TetrisModel` has a game status variable that can take on the
    following values:
    `BEFORE_GAME, PLAYING, PAUSED, GAME_OVER, AFTER_GAME.`
  - This is useful because various views can listen for changes to the
    game status value of the `TetrisModel` and update appropriately.

# Game Stats and High Scores
- `GameStatisticsView`
  - When the `AppController` is notified that the `TetrisModel` has status
    `GAME_OVER`, or when the game is aborted, it closes the tetris game and
    swaps the visible view to the `GameStatisticsView`. This view has a
    reference to the `TetrisModel` of some tetris game (it could be a game
    that has ended, like in the current usage, but technically we could also
    display the game stats as the game occurs). It shows all of a game's
    statistics about lines cleared, minos dropped, and the like. And it has a
    text input for adding the game score to the high scores database.

- `HighScoresModel`
  - This class contains all of the high scores data. When the app starts up,
    an instance of `HighScoresModel` fetches the top 100 scores from MongoDB.
    When later scores are added from the application itself, the new scores
    are added through a `HighScoresModel`, which will post scores to the
    database and, when the post is successful, update the scores data that
    a `HighScoresView` needs to render itself.

- `HighScoresView`
  - This is the panel that the `AppController` makes visible when the user
    navigates to (or is navigated to) the high scores screen. A
    `HighScoresView` instance has a reference to a `HighScoresModel`, from
    which it receives property change events that prompt it to refresh the
    table data.

- `HTTPUtilities`
  - Who knew GET-ing and POST-ing JSON could be so cumbersome? Not me,
    so I made this utility class that the `HighScoresModel` uses to GET the
    scores JSON and to POST new scores.
- `JsonHandler`
  - All this is is an object with one function that takes in a JsonArray and
    returns void. I use it as sort of makeshift callback function for when
    I want to wait until the score POST has been successful before updating
    the list of scores that the view draws. I'm sure there's a better way to
    do this, but for now I don't think it's doing any harm.

## Other Classes

- `Sound`
  - This is a utility class with a static method for playing sound clips.

- `GameElementPanel`, `TetrisUIButton`, `TetrisUILabel` `TetrisUIPanel`
  - These classes extend JPanel, JButton, JLabel, and JPanel, respectively.
    Their function is to 1) let me avoid writing the same JComponent
    styling/configuration code every time I make a new component, and 2) so
    that the application has a unified style that, if I ever wanted to change,
    I easily could by modifying these classes.
