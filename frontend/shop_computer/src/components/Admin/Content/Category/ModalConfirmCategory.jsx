import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";

const ModalConfirmCategory = (props) => {
  const { show, setShow, handleModalDeleteCategory } = props;

  const handleClose = () => {
    setShow(false);
  };

  const handleDeleteCategory = () => {
    handleModalDeleteCategory();
    handleClose();
  };

  return (
    <Modal
      show={show}
      onHide={handleClose}
      size="lg"
      aria-labelledby="contained-modal-title-vcenter"
      centered
    >
      <Modal.Header closeButton>
        <Modal.Title id="contained-modal-title-vcenter">
          Delete Category
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <h2>Are you sure you want to delete this category?</h2>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button variant="primary" onClick={() => handleDeleteCategory()}>
          Corfirm
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModalConfirmCategory;
