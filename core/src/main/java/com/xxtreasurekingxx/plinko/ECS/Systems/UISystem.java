package com.xxtreasurekingxx.plinko.ECS.Systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.xxtreasurekingxx.plinko.Core;
import com.xxtreasurekingxx.plinko.ECS.Components.DataComponent;
import com.xxtreasurekingxx.plinko.ECS.Components.InventoryComponent;
import com.xxtreasurekingxx.plinko.ECS.Components.MapComponent;
import com.xxtreasurekingxx.plinko.ECS.Components.UIComponent;
import com.xxtreasurekingxx.plinko.ECS.ECSEngine;
import com.xxtreasurekingxx.plinko.LayeredDrawable;
import com.xxtreasurekingxx.plinko.Map.*;

import static com.xxtreasurekingxx.plinko.Core.GAMEH;
import static com.xxtreasurekingxx.plinko.Map.Balls.BASE;
import static com.xxtreasurekingxx.plinko.Map.Resources.METAL_HUNK;

public class UISystem extends IteratingSystem {
    private final Core game;
    private final FitViewport viewport;
    private final ECSEngine engine;
    private Stage stage;
    private AssetManager assetManager;
    private TextureAtlas atlas;
    private BitmapFont font;
    private DragAndDrop dragAndDrop;

    private InventoryComponent inventoryComponent;

    private Entity mapEntity;
    private MapComponent mapComponent;
    private Entity dataEntity;
    private DataComponent dataComponent;

    private Table healthDisplayTable;
    private TextButton.TextButtonStyle healthDisplayStyle;

    private Stack uiStack;
    private ButtonGroup mainUIButtonGroup;
    private Label.LabelStyle titleDisplayStyle;
    private Label.LabelStyle blankLabelStyle;
    private Label.LabelStyle clearLabelStyle;
    private TextButton.TextButtonStyle indexSlotStyle;
    private TextButton.TextButtonStyle ballStyle;
    private TextButton.TextButtonStyle pegStyle;
    private TextButton.TextButtonStyle monsterStyle;
    private TextButton.TextButtonStyle resourceStyle;

    private Image ballDisplay;

    //crafting ui
    private Array<Item> craftingItems;
    private Table craftingTable;
    private Table craftingInventoryTable;
    private Label craftUIArrow;
    private Label.LabelStyle craftUIArrowStyle;
    private Label ballCraftingSlot;
    private Label.LabelStyle ballCraftingSlotStyle;
    private Label pegCraftingSlot;
    private Label.LabelStyle pegCraftingSlotStyle;
    private Label resourceCraftingSlot;
    private Label.LabelStyle resourceCraftingSlotStyle;
    private Label outputCraftingSlot;
    private Label.LabelStyle outputCraftingSlotStyle;
    private TextButton completeCraftButton;
    private TextButton.TextButtonStyle completeCraftButtonStyle;
    private TextButton clearCraftButton;
    private TextButton.TextButtonStyle clearCraftButtonStyle;
    private Image ballSlotItem;
    private Image pegSlotItem;
    private Image resourceSlotItem;
    private Image outputSlotItem;
    private boolean resourceCraftingSlotHasItem;
    private boolean ballCraftingSlotHasItem;
    private boolean pegCraftingSlotHasItem;

    //bag ui
    private Table bagTable;
    private ButtonGroup bagButtonGroup;
    private ButtonGroup bagIconGroup;
    private Label bagTitleDisplay;
    private Label bagInfoDisplay;
    private Stack bagStack;

    private Table ballBagTable;
    private TextButton ballBagButton;
    private Table pegBagTable;
    private TextButton pegBagButton;
    private Table resourceBagTable;
    private TextButton resourceBagButton;

    //index ui
    private Table indexTable;
    private ButtonGroup indexButtonGroup;
    private Label indexTitleDisplay;
    private Label indexInfoDisplay;
    private Stack indexStack;

    private Table ballIndexTable;
    private TextButton ballIndexButton;
    private Table pegIndexTable;
    private TextButton pegIndexButton;
    private Table monsterIndexTable;
    private TextButton monsterIndexButton;

    private TextButton indexButton;
    private TextButton.TextButtonStyle indexButtonStyle;

    private TextButton bagButton;
    private TextButton.TextButtonStyle bagButtonStyle;

    private TextButton craftButton;
    private TextButton.TextButtonStyle craftButtonStyle;

    public UISystem(final Core game, final FitViewport viewport, final ECSEngine engine) {
        super(Family.all(UIComponent.class).get());
        this.game = game;
        this.viewport = viewport;
        this.engine = engine;
        stage = new Stage(viewport, game.getBatch());
        game.getInputMultiplexer().addProcessor(0, stage);

        Gdx.input.setInputProcessor(game.getInputMultiplexer()); //re set multiplexer to update the stage has been added???

        assetManager = game.getAssetManager();

        initFiles();
        initStyles();

        //stage.setDebugAll(true);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        mapEntity = engine.getEntitiesFor(Family.all(MapComponent.class).get()).first();
        mapComponent = mapEntity.getComponent(MapComponent.class);

        dataEntity = engine.getEntitiesFor(Family.all(DataComponent.class).get()).first();
        dataComponent = dataEntity.getComponent(DataComponent.class);

        inventoryComponent = engine.getEntitiesFor(Family.all(InventoryComponent.class).get()).first().getComponent(InventoryComponent.class);

        int healthCheck = dataComponent.health - 10;
        healthDisplayTable = new Table();
        healthDisplayTable.setSize(180, 16);
        healthDisplayTable.setPosition(436, GAMEH - 25 - healthDisplayTable.getHeight());
        for(int i = 0; i < 10; i++) {
            TextButton healthDisplay = new TextButton("", healthDisplayStyle);
            if(healthCheck < 0) {
                healthCheck++;
                healthDisplay.setDisabled(true);
            }
            healthDisplayTable.add(healthDisplay).size(18, 14);
        }
        stage.addActor(healthDisplayTable);

        indexButton = new TextButton("", indexButtonStyle);
        indexButton.setPosition(10, GAMEH - 10 - indexButton.getHeight());
        indexButton.setChecked(true);
        indexButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showIndexUI();
            }
        });

        craftButton = new TextButton("", craftButtonStyle);
        craftButton.setPosition(34, GAMEH - 10 - craftButton.getHeight());
        craftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showCraftingUI();
            }
        });

        bagButton = new TextButton("", bagButtonStyle);
        bagButton.setPosition(58, GAMEH - 10 - bagButton.getHeight());
        bagButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showBagUI();
            }
        });

        ballDisplay = new Image(new TextureRegionDrawable(atlas.findRegion("metalHunkIcon.png")));

        initTables();

        initIndexUI();
        initBagUI();
        initCraftingUI();

        initDragAndDrop();

        stage.addActor(craftButton);
        stage.addActor(craftingTable);
        stage.addActor(indexButton);
        stage.addActor(indexTable);
        stage.addActor(bagButton);
        stage.addActor(bagTable);
        stage.addActor(ballDisplay);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime); // under the hood processEntity calling

        int healthCheck = dataComponent.health - 10;

        for(int i = 0; i < healthDisplayTable.getChildren().size; i++) {
            if(healthDisplayTable.getChild(i) instanceof TextButton) {
                if(healthCheck < 0) {
                    healthCheck++;
                    ((TextButton) healthDisplayTable.getChild(i)).setDisabled(true);
                }
            }
        }

        //todo set active ball to buttongroup index

        updateBallDisplay();
        //updateBagUI();
        updateCraftingUI();
        updateInventoryUI();

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    private void initStyles() {
        blankLabelStyle = new Label.LabelStyle();
        blankLabelStyle.font = font;
        blankLabelStyle.background = new TextureRegionDrawable(atlas.findRegion("blank.png"));

        clearLabelStyle = new Label.LabelStyle();
        clearLabelStyle.font = font;
        clearLabelStyle.background = new TextureRegionDrawable(atlas.findRegion("clear.png"));

        titleDisplayStyle = new Label.LabelStyle();
        titleDisplayStyle.font = font;
        titleDisplayStyle.background = new TextureRegionDrawable(atlas.findRegion("titleDisplay.png"));

        indexSlotStyle = new TextButton.TextButtonStyle();
        indexSlotStyle.font = font;
        indexSlotStyle.up = new TextureRegionDrawable(atlas.findRegion("indexSlot.png"));
        indexSlotStyle.disabled = new TextureRegionDrawable(atlas.findRegion("indexSlotDisabled.png"));

        healthDisplayStyle = new TextButton.TextButtonStyle();
        healthDisplayStyle.font = font;
        healthDisplayStyle.up = new TextureRegionDrawable(atlas.findRegion("healthDisplayIconUp.png"));
        healthDisplayStyle.disabled = new TextureRegionDrawable(atlas.findRegion("healthDisplayIconDown.png"));

        indexButtonStyle = new TextButton.TextButtonStyle();
        indexButtonStyle.font = font;
        indexButtonStyle.up = new TextureRegionDrawable(atlas.findRegion("indexButtonUp.png"));
        indexButtonStyle.checked = new TextureRegionDrawable(atlas.findRegion("indexButtonDown.png"));

        craftButtonStyle = new TextButton.TextButtonStyle();
        craftButtonStyle.font = font;
        craftButtonStyle.up = new TextureRegionDrawable(atlas.findRegion("craftButtonUp.png"));
        craftButtonStyle.checked = new TextureRegionDrawable(atlas.findRegion("craftButtonDown.png"));

        bagButtonStyle = new TextButton.TextButtonStyle();
        bagButtonStyle.font = font;
        bagButtonStyle.up = new TextureRegionDrawable(atlas.findRegion("bagButtonUp.png"));
        bagButtonStyle.checked = new TextureRegionDrawable(atlas.findRegion("bagButtonDown.png"));

        ballStyle = new TextButton.TextButtonStyle();
        ballStyle.font = font;
        ballStyle.up = new TextureRegionDrawable(atlas.findRegion("ballIndexButtonUp.png"));
        ballStyle.checked = new TextureRegionDrawable(atlas.findRegion("ballIndexButtonDown.png"));

        pegStyle = new TextButton.TextButtonStyle();
        pegStyle.font = font;
        pegStyle.up = new TextureRegionDrawable(atlas.findRegion("pegIndexButtonUp.png"));
        pegStyle.checked = new TextureRegionDrawable(atlas.findRegion("pegIndexButtonDown.png"));

        monsterStyle = new TextButton.TextButtonStyle();
        monsterStyle.font = font;
        monsterStyle.up = new TextureRegionDrawable(atlas.findRegion("monsterIndexButtonUp.png"));
        monsterStyle.checked = new TextureRegionDrawable(atlas.findRegion("monsterIndexButtonDown.png"));

        resourceStyle = new TextButton.TextButtonStyle();
        resourceStyle.font = font;
        resourceStyle.up = new TextureRegionDrawable(atlas.findRegion("resourceButtonUp.png"));
        resourceStyle.checked = new TextureRegionDrawable(atlas.findRegion("resourceButtonDown.png"));

        ballCraftingSlotStyle = new Label.LabelStyle();
        ballCraftingSlotStyle.font = font;
        ballCraftingSlotStyle.background = new TextureRegionDrawable(atlas.findRegion("craftingBallSlot.png"));

        resourceCraftingSlotStyle = new Label.LabelStyle();
        resourceCraftingSlotStyle.font = font;
        resourceCraftingSlotStyle.background = new TextureRegionDrawable(atlas.findRegion("craftingResourceSlot.png"));

        pegCraftingSlotStyle = new Label.LabelStyle();
        pegCraftingSlotStyle.font = font;
        pegCraftingSlotStyle.background = new TextureRegionDrawable(atlas.findRegion("craftingPegSlot.png"));

        outputCraftingSlotStyle = new Label.LabelStyle();
        outputCraftingSlotStyle.font = font;
        outputCraftingSlotStyle.background = new TextureRegionDrawable(atlas.findRegion("craftingOutputSlot.png"));

        craftUIArrowStyle = new Label.LabelStyle();
        craftUIArrowStyle.font = font;
        craftUIArrowStyle.background = new TextureRegionDrawable(atlas.findRegion("craftUILabel.png"));

        completeCraftButtonStyle = new TextButton.TextButtonStyle();
        completeCraftButtonStyle.font = font;
        completeCraftButtonStyle.up = new TextureRegionDrawable(atlas.findRegion("finishCraftButtonUp.png"));
        completeCraftButtonStyle.down = new TextureRegionDrawable(atlas.findRegion("finishCraftButtonDown.png"));

        clearCraftButtonStyle = new TextButton.TextButtonStyle();
        clearCraftButtonStyle.font = font;
        clearCraftButtonStyle.up = new TextureRegionDrawable(atlas.findRegion("clearCraftButtonUp.png"));
        clearCraftButtonStyle.down = new TextureRegionDrawable(atlas.findRegion("clearCraftButtonDown.png"));
    }

    private void initTables() {
        uiStack = new Stack();
        mainUIButtonGroup = new ButtonGroup();
        mainUIButtonGroup.setMinCheckCount(1);
        mainUIButtonGroup.setMaxCheckCount(1);

        indexTable = new Table();
        indexTable.setLayoutEnabled(false);

        craftingTable = new Table();
        craftingTable.setLayoutEnabled(false);
        craftingTable.setVisible(false);

        bagTable = new Table();
        bagTable.setLayoutEnabled(false);
        bagTable.setVisible(false);
        bagTable.setTouchable(Touchable.childrenOnly);

        uiStack.add(craftingTable);
        uiStack.add(bagTable);
        uiStack.add(indexTable);

        mainUIButtonGroup.add(craftButton);
        mainUIButtonGroup.add(bagButton);
        mainUIButtonGroup.add(indexButton);
    }

    private void initIndexUI() {
        indexButtonGroup = new ButtonGroup();
        indexButtonGroup.setMaxCheckCount(1);
        indexButtonGroup.setMinCheckCount(1);

        indexTitleDisplay = new Label(" ", titleDisplayStyle);
        indexTitleDisplay.setSize(75, 14);
        indexTitleDisplay.setPosition(7, GAMEH - 52 - indexTitleDisplay.getHeight());
        indexTitleDisplay.setFontScale(0.75f);

        indexInfoDisplay = new Label(" ", blankLabelStyle);
        indexInfoDisplay.setSize(75, 72);
        indexInfoDisplay.setPosition(7, GAMEH - 67 - indexInfoDisplay.getHeight());
        indexInfoDisplay.setFontScale(0.5f);
        indexInfoDisplay.setWrap(true);

        indexStack = new Stack();
        indexStack.setWidth(75);
        indexStack.setPosition(7, GAMEH - 140 - indexStack.getHeight());

        //ball index ui
        ballIndexTable = new Table();
        ballIndexTable.setVisible(true);
        ballIndexTable.top();
        int ballIndex = 1;
        for(Balls ball : Balls.values()) {
            TextButton.TextButtonStyle ballStyle = new TextButton.TextButtonStyle();
            ballStyle.font = font;
            ballStyle.up = new TextureRegionDrawable(atlas.findRegion(ball.getBagIcon()));
            ballStyle.disabled = new TextureRegionDrawable(atlas.findRegion("indexSlotDisabled.png"));

            TextButton button = new TextButton("", ballStyle);
            button.setDisabled(!ball.isUnlocked());
            if(!ball.isUnlocked()) {

            }
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    indexTitleDisplay.setText(" ???");
                    indexInfoDisplay.setText("Unlocks after completing level..."); // "Unlock this ball to see the description"

                    if(!ball.isUnlocked()) return;

                    indexTitleDisplay.setText(" " + ball.getName());
                    indexInfoDisplay.setText(ball.getDescription());
                }
            });

            if(ballIndex % 4 == 0) {
                ballIndexTable.add(button).padBottom(1).row();
            } else {
                ballIndexTable.add(button).pad(0, 0, 1, 1);
            }
            ballIndex++;
        }

        ballIndexButton = new TextButton("", ballStyle);
        ballIndexButton.setSize(21, 14);
        ballIndexButton.setPosition(10, GAMEH - 35 - ballIndexButton.getHeight());
        ballIndexButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showBallIndexUI();
            }
        });

        //peg index ui
        pegIndexTable = new Table();
        pegIndexTable.setVisible(false);
        pegIndexButton = new TextButton("", pegStyle);
        pegIndexButton.setSize(21, 14);
        pegIndexButton.setPosition(34, GAMEH - 35 - pegIndexButton.getHeight());
        pegIndexButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showPegIndexUI();
            }
        });

        //monster index ui
        monsterIndexTable = new Table();
        monsterIndexTable.setVisible(false);
        monsterIndexButton = new TextButton("", monsterStyle);
        monsterIndexButton.setSize(21, 14);
        monsterIndexButton.setPosition(58, GAMEH - 35 - monsterIndexButton.getHeight());
        monsterIndexButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showMonsterIndexUI();
            }
        });

        indexButtonGroup.add(ballIndexButton);
        indexButtonGroup.add(pegIndexButton);
        indexButtonGroup.add(monsterIndexButton);

        indexStack.add(monsterIndexTable);
        indexStack.add(pegIndexTable);
        indexStack.add(ballIndexTable);

        indexTable.add(ballIndexButton);
        indexTable.add(pegIndexButton);
        indexTable.add(monsterIndexButton);
        indexTable.add(indexTitleDisplay);
        indexTable.add(indexInfoDisplay);
        indexTable.add(indexStack);
    }

    private void showBallIndexUI() {
        ballIndexTable.setVisible(true);
        pegIndexTable.setVisible(false);
        monsterIndexTable.setVisible(false);
    }

    private void showPegIndexUI() {
        ballIndexTable.setVisible(false);
        pegIndexTable.setVisible(true);
        monsterIndexTable.setVisible(false);
    }

    private void showMonsterIndexUI() {
        ballIndexTable.setVisible(false);
        pegIndexTable.setVisible(false);
        monsterIndexTable.setVisible(true);
    }

    private void initBagUI() {
        bagButtonGroup = new ButtonGroup();
        bagButtonGroup.setMaxCheckCount(1);
        bagButtonGroup.setMinCheckCount(1);

        bagTitleDisplay = new Label(" ", titleDisplayStyle);
        bagTitleDisplay.setSize(75, 14);
        bagTitleDisplay.setPosition(7, GAMEH - 52 - bagTitleDisplay.getHeight());
        bagTitleDisplay.setFontScale(0.75f);

        bagInfoDisplay = new Label(" ", blankLabelStyle);
        bagInfoDisplay.setSize(75, 72);
        bagInfoDisplay.setPosition(7, GAMEH - 67 - bagInfoDisplay.getHeight());
        bagInfoDisplay.setFontScale(0.5f);
        bagInfoDisplay.setWrap(true);

        bagStack = new Stack();
        bagStack.setWidth(75);
        bagStack.setPosition(7, GAMEH - 52 - bagStack.getHeight());

        bagIconGroup = new ButtonGroup<>();
        bagIconGroup.setMaxCheckCount(1);
        bagIconGroup.setMinCheckCount(0);

        //ball bag ui
        ballBagTable = new Table();
        ballBagTable.setVisible(true);
        ballBagTable.top();
        ballBagTable.left();

        ballBagButton = new TextButton("", ballStyle);
        ballBagButton.setSize(21, 14);
        ballBagButton.setPosition(10, GAMEH - 35 - ballBagButton.getHeight());
        ballBagButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showBallBagUI();
            }
        });

        //peg bag ui
        pegBagTable = new Table();
        pegBagTable.setVisible(false);
        pegBagButton = new TextButton("", pegStyle);
        pegBagButton.setSize(21, 14);
        pegBagButton.setPosition(34, GAMEH - 35 - pegBagButton.getHeight());
        pegBagButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showPegBagUI();
            }
        });

        //resource bag ui
        resourceBagTable = new Table();
        resourceBagTable.setVisible(false);
        resourceBagButton = new TextButton("", resourceStyle);
        resourceBagButton.setSize(21, 14);
        resourceBagButton.setPosition(58, GAMEH - 35 - resourceBagButton.getHeight());
        resourceBagButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showResourceBagUI();
            }
        });

        bagButtonGroup.add(ballBagButton);
        bagButtonGroup.add(pegBagButton);
        bagButtonGroup.add(resourceBagButton);

        bagStack.add(resourceBagTable);
        bagStack.add(pegBagTable);
        bagStack.add(ballBagTable);

        bagTable.add(ballBagButton);
        bagTable.add(pegBagButton);
        bagTable.add(resourceBagButton);
        bagTable.add(bagStack);
    }

    private void showBallBagUI() {
        ballBagTable.setVisible(true);
        pegBagTable.setVisible(false);
        resourceBagTable.setVisible(false);
    }

    private void showPegBagUI() {
        ballBagTable.setVisible(false);
        pegBagTable.setVisible(true);
        resourceBagTable.setVisible(false);
    }

    private void showResourceBagUI() {
        ballBagTable.setVisible(false);
        pegBagTable.setVisible(false);
        resourceBagTable.setVisible(true);
    }

    private void initCraftingUI() {
        craftingInventoryTable = new Table();
        craftingInventoryTable.setVisible(true);
        craftingInventoryTable.setWidth(75);
        craftingInventoryTable.setPosition(7, GAMEH - 102 - craftingInventoryTable.getHeight() - 10);
        craftingInventoryTable.left();

        craftingItems = new Array<>();

        pegCraftingSlot = new Label(" ", pegCraftingSlotStyle);
        pegCraftingSlot.setPosition(59, GAMEH - 35 - pegCraftingSlot.getHeight());

        ballCraftingSlot = new Label(" ", ballCraftingSlotStyle);
        ballCraftingSlot.setPosition(9, GAMEH - 35 - ballCraftingSlot.getHeight());

        resourceCraftingSlot = new Label(" ", resourceCraftingSlotStyle);
        resourceCraftingSlot.setSize(21, 21);
        resourceCraftingSlot.setPosition(34, GAMEH - 35 - resourceCraftingSlot.getHeight());

        craftUIArrow = new Label(" ", craftUIArrowStyle);
        craftUIArrow.setPosition(19, GAMEH - 58 - craftUIArrow.getHeight());

        outputCraftingSlot = new Label(" ", outputCraftingSlotStyle);
        outputCraftingSlot.setSize(21, 21);
        outputCraftingSlot.setPosition(34, GAMEH - 78 - outputCraftingSlot.getHeight());

        completeCraftButton = new TextButton("", completeCraftButtonStyle);
        completeCraftButton.setSize(16, 17);
        completeCraftButton.setPosition(61, GAMEH - 80 - completeCraftButton.getHeight());
        completeCraftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            for(Recipes recipe : Recipes.values()) {
                if(containsExact(recipe.getRecipe(), craftingItems)) {
                    for(Item item : recipe.getRecipe()) {
                        inventoryComponent.inventory.getAndIncrement(item, 0, -1);
                    }
                    inventoryComponent.inventory.getAndIncrement(recipe.getOutput(), 0, 1);
                    inventoryComponent.updateInventoryUI = true;
                    clearCraftingSlots();
                }
            }
            }
        });

        clearCraftButton = new TextButton("", clearCraftButtonStyle);
        clearCraftButton.setSize(16, 17);
        clearCraftButton.setPosition(12, GAMEH - 80 - clearCraftButton.getHeight());
        clearCraftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearCraftingSlots();
            }
        });

        resourceCraftingSlotHasItem = false;
        resourceSlotItem = new Image(new TextureRegionDrawable(atlas.findRegion("clear.png")));
        resourceSlotItem.setSize(18, 18);
        resourceSlotItem.setPosition(36, GAMEH - 37 - resourceSlotItem.getHeight());
        resourceSlotItem.setTouchable(Touchable.disabled);

        ballCraftingSlotHasItem = false;
        ballSlotItem = new Image(new TextureRegionDrawable(atlas.findRegion("clear.png")));
        ballSlotItem.setSize(18, 18);
        ballSlotItem.setPosition(11, GAMEH - 37 - ballSlotItem.getHeight());
        ballSlotItem.setTouchable(Touchable.disabled);

        pegCraftingSlotHasItem = false;
        pegSlotItem = new Image(new TextureRegionDrawable(atlas.findRegion("clear.png")));
        pegSlotItem.setSize(18, 18);
        pegSlotItem.setPosition(61, GAMEH - 37 - pegSlotItem.getHeight());
        pegSlotItem.setTouchable(Touchable.disabled);

        outputSlotItem = new Image(new TextureRegionDrawable(atlas.findRegion("clear.png")));
        outputSlotItem.setSize(18, 18);
        outputSlotItem.setPosition(36, GAMEH - 80 - outputSlotItem.getHeight());

        craftingTable.add(ballCraftingSlot);
        craftingTable.add(resourceCraftingSlot);
        craftingTable.add(pegCraftingSlot);
        craftingTable.add(outputCraftingSlot);
        craftingTable.add(craftingInventoryTable);
        craftingTable.add(craftUIArrow);
        craftingTable.add(completeCraftButton);
        craftingTable.add(clearCraftButton);
        craftingTable.add(resourceSlotItem);
        craftingTable.add(ballSlotItem);
        craftingTable.add(pegSlotItem);
        craftingTable.add(outputSlotItem);
    }

    private void clearCraftingSlots() {
        craftingItems.clear();
        resourceCraftingSlotHasItem = false;
        ballCraftingSlotHasItem = false;
        pegCraftingSlotHasItem = false;

        pegSlotItem.setDrawable(new TextureRegionDrawable(atlas.findRegion("clear.png")));
        resourceSlotItem.setDrawable(new TextureRegionDrawable(atlas.findRegion("clear.png")));
        ballSlotItem.setDrawable(new TextureRegionDrawable(atlas.findRegion("clear.png")));
        outputSlotItem.setDrawable(new TextureRegionDrawable(atlas.findRegion("clear.png")));
    }

    private void showIndexUI() {
        indexTable.setVisible(true);
        bagTable.setVisible(false);
        craftingTable.setVisible(false);
    }

    private void showBagUI() {
        indexTable.setVisible(false);
        bagTable.setVisible(true);
        craftingTable.setVisible(false);
    }

    private void showCraftingUI() {
        indexTable.setVisible(false);
        bagTable.setVisible(false);
        craftingTable.setVisible(true);
    }

    private void updateBagUI() {

    }

    private void updateCraftingUI() {
        for(ObjectIntMap.Entry<Item> entry : inventoryComponent.inventory) {
            Item item = entry.key;
            if(entry.value <= 0) {
                inventoryComponent.inventory.remove(item, 0);
            }
        }

        for(Recipes recipe : Recipes.values()) {
            if(containsExact(recipe.getRecipe(), craftingItems)) {
                outputSlotItem.setDrawable(new TextureRegionDrawable(atlas.findRegion(recipe.getResultOutputImage())));
                outputSlotItem.pack();
                break;
            } else {
                outputSlotItem.setDrawable(new TextureRegionDrawable(atlas.findRegion("clear.png")));
                outputSlotItem.pack();
            }
        }
    }

    private void updateInventoryUI() {
        if(!inventoryComponent.updateInventoryUI) {
            return;
        }

        inventoryComponent.updateInventoryUI = false;
        ballBagTable.clear();
        int inventoryIndex = 1;
        if(inventoryComponent.inventory.size > 0) {
            for (ObjectIntMap.Entry<Item> entry : inventoryComponent.inventory) {
                Item item = entry.key;

                Drawable icon = new TextureRegionDrawable(atlas.findRegion(item.getBagIcon()));
                TextButton.TextButtonStyle inventoryStyle = new TextButton.TextButtonStyle();
                inventoryStyle.font = font;
                inventoryStyle.up = icon;

                TextButton button = new TextButton("\n     " + entry.value, inventoryStyle);
                button.getLabel().setFontScale(0.5f);
                button.setUserObject(item);

                if(item instanceof Balls) {
                    bagIconGroup.add(button);
                    inventoryStyle.checked = new LayeredDrawable(icon, new TextureRegionDrawable(atlas.findRegion("bagIconFrame.png")));
                    button.setStyle(inventoryStyle);
                }
                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        System.out.println("button clicked");
                    }
                });

                if(item == inventoryComponent.selectedItem) {
                    button.setChecked(true);
                }

                if(inventoryIndex % 4 == 0) {
                    ballBagTable.add(button).padBottom(1).row();
                } else {
                    ballBagTable.add(button).pad(0, 0, 1, 1);
                }
                inventoryIndex++;
            }

        }

        //crafting
        craftingInventoryTable.clear();
        int craftingIndex = 1;
        if(inventoryComponent.inventory.size > 0) {
            for (ObjectIntMap.Entry<Item> entry : inventoryComponent.inventory) {
                Item item = entry.key;

                TextButton.TextButtonStyle inventoryStyle = new TextButton.TextButtonStyle();
                inventoryStyle.font = font;
                inventoryStyle.up = new TextureRegionDrawable(atlas.findRegion(item.getBagIcon()));

                TextButton button = new TextButton("\n     " + entry.value, inventoryStyle);
                button.getLabel().setFontScale(0.5f);
                setDraggable(button, item);

                if (craftingIndex % 4 == 0) {
                    craftingInventoryTable.add(button).size(18, 18).padBottom(1).row();
                } else {
                    craftingInventoryTable.add(button).size(18, 18).pad(0, 0, 1, 1);
                }
                craftingIndex++;
            }
        }
    }

    private void setDraggable(Actor actor, Item item) {
        dragAndDrop.addSource(new DragAndDrop.Source(actor) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                payload.setObject(item);
                Image dragImage = new Image(new TextureRegionDrawable(atlas.findRegion(item.getIcon())));
                payload.setDragActor(dragImage);
                return payload;
            }
        });
    }

    private void initDragAndDrop() {
        dragAndDrop = new DragAndDrop();
        dragAndDrop.setDragTime(0);
        dragAndDrop.setDragActorPosition(8, -8);
        dragAndDrop.addTarget(new DragAndDrop.Target(resourceCraftingSlot) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                if(payload.getObject() instanceof Resources) {
                    return true;
                }
                return false;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                Item item = (Item) payload.getObject();
                if(!resourceCraftingSlotHasItem) {
                    resourceCraftingSlotHasItem = true;
                    craftingItems.add(item);
                    resourceSlotItem.setDrawable(new TextureRegionDrawable(atlas.findRegion(item.getIcon())));
                    resourceSlotItem.pack();
                }
            }
        });
        dragAndDrop.addTarget(new DragAndDrop.Target(ballCraftingSlot) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                if(payload.getObject() instanceof Balls) {
                    return true;
                }
                return false;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                Item item = (Item) payload.getObject();
                ballSlotItem.setDrawable(new TextureRegionDrawable(atlas.findRegion(item.getIcon())));
                ballSlotItem.pack();
                if(!ballCraftingSlotHasItem) {
                    ballCraftingSlotHasItem = true;
                    craftingItems.add(item);
                    ballSlotItem.setDrawable(new TextureRegionDrawable(atlas.findRegion(item.getIcon())));
                    ballSlotItem.pack();
                }
            }
        });
        dragAndDrop.addTarget(new DragAndDrop.Target(pegSlotItem) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                if(payload.getObject() instanceof Pegs) {
                    return true;
                }
                return false;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                Item item = (Item) payload.getObject();
                pegSlotItem.setDrawable(new TextureRegionDrawable(atlas.findRegion(item.getIcon())));
                pegSlotItem.pack();
                if(!pegCraftingSlotHasItem) {
                    craftingItems.add(item);
                    pegCraftingSlotHasItem = true;
                }
                System.out.println(craftingItems);
            }
        });
    }

    private boolean containsExact(final Array<Item> array1, final Array<Item> array2) {
        if(array1.size != array2.size) return false;
        return array1.containsAll(array2, true);
    }

    private void updateBallDisplay() {
        inventoryComponent.selectedItem = null;
        ballDisplay.setDrawable(new TextureRegionDrawable(atlas.findRegion("clear.png")));
        if(bagIconGroup.getChecked() != null && bagIconGroup.getChecked().getUserObject() != null) {
            inventoryComponent.selectedItem = (Item) bagIconGroup.getChecked().getUserObject();
            if(inventoryComponent.inventory.get(inventoryComponent.selectedItem, 0) <= 0) {
               return;
            }

            Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            mousePos.set(viewport.unproject(mousePos));

            float minX = mapComponent.dropArea.x;
            float maxX = mapComponent.dropArea.x + mapComponent.dropArea.width;
            float minY = mapComponent.dropArea.y;
            float maxY = mapComponent.dropArea.y + mapComponent.dropArea.height;

            mousePos.x = Math.max(minX, mousePos.x);
            mousePos.x = Math.min(maxX, mousePos.x);

            mousePos.y = Math.max(minY, mousePos.y);
            mousePos.y = Math.min(maxY, mousePos.y);

            Item item = (Item) bagIconGroup.getChecked().getUserObject();
            ballDisplay.setDrawable(new TextureRegionDrawable(atlas.findRegion(item.getIcon())));
            ballDisplay.setPosition(mousePos.x - ballDisplay.getWidth()/2, mousePos.y - ballDisplay.getHeight()/2);
        }
    }

    private void initFiles() {
        atlas = assetManager.get("ui/main.atlas");
        font = new BitmapFont();
        font.setColor(Color.BLACK);
    }

    public Stage getStage() {
        return stage;
    }
}
