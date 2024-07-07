import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";

const ModalConfirm = (props) => {
  const { show, setShow, handleModalDeleteUser } = props;

  const handleClose = () => {
    setShow(false);
  };

  const handleDeleteUser = () => {
    handleModalDeleteUser();
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
          Delete User
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <h2>Are you sure you want to delete this user?</h2>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Close
        </Button>
        <Button variant="primary" onClick={() => handleDeleteUser()}>
          Corfirm
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModalConfirm;
