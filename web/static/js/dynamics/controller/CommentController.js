var CommentController = function CommentController(model, view, parentController) {
  this.model = model;
  this.view = view;
  this.parentController = parentController;
  this.init();
  this.autohideCells = [ "buttons" ];
};

